package io.github.lsmcodes.notes_api.controller.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.lsmcodes.notes_api.dto.model.user.UserRequestDTO;
import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link UserController} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @MockBean
    private VerificationService verificationService;

    /**
     * Tests the
     * {@link UserController#createUser(UserRequestDTO dto, BindingResult result)}
     * endpoint method to ensure it correctly creates a new user.
     * 
     * @throws Exception if an error occurs while creating a user.
     */
    @Test
    @Order(1)
    @DisplayName("UserController createUser should create a new user.")
    public void createUser_ShouldCreateANewUser() throws Exception {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("Default User", "default_user", "1234567890");
        User user = userRequestDTO.DTOToEntity();
        user.setRole(UserRole.ROLE_USER);

        Mockito.when(this.bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(this.userService.save(user)).thenReturn(user);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/notes-api/users")
                .content(new ObjectMapper().writeValueAsString(userRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.role").value(user.getRole().getValue()));
    }

    /**
     * Tests the {@link UserController#getLoggedInUser()} endpoint method to ensure
     * it correctly returns the logged in user.
     * 
     * @throws Exception if an error occurs while searching for the logged-in user.
     */
    @Test
    @Order(2)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("UserController getLoggedInUser should return the logged-in user.")
    public void getLoggedInUser_ShouldReturnLoggedInUser() throws Exception {
        // Arrange
        User user = NotesApiUtil.getNewUser();

        Mockito.doNothing().when(verificationService).verifyIfUserExistsByUsername(user.getUsername());
        Mockito.when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/notes-api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.role").value(user.getRole().getValue()));
    }

    /**
     * Tests the
     * {@link UserController#updateLoggedInUser(UserRequestDTO dto, BindingResult result)}
     * endpoint method to ensure it correctly updates the logged-in user.
     * 
     * @throws Exception if an error occurs while updating the logged in user.
     */
    @Test
    @Order(3)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("UserController updateLoggedInUser should return the updated logged-in user.")
    public void updateLoggedInUser_ShouldUpdateAndReturnLoggedInUser() throws Exception {
        // Arrange
        User user = NotesApiUtil.getNewUser();

        Mockito.when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserRequestDTO updatedUserDetails = new UserRequestDTO("Updated User", "updated_user", "1234567890");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/notes-api/users")
                .content(new ObjectMapper().writeValueAsString(updatedUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(updatedUserDetails.getName()))
                .andExpect(jsonPath("$.data.username").value(updatedUserDetails.getUsername()));
    }

    /**
     * Tests the {@link UserController#deleteLoggedInUser()} endpoint method to
     * ensure it correctly deletes the logged-in user.
     * 
     * @throws Exception if an error occurs while deleting the logged-in user.
     */
    @Test
    @Order(4)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("UserController deleteLoggedInUser should delete the logged-in user and return a message.")
    public void deleteLoggedInUser_ShouldDeleteLoggedInUserAndReturnAMessage() throws Exception {
        // Arrange
        User user = NotesApiUtil.getNewUser();

        Mockito.doNothing().when(verificationService).verifyIfUserExistsByUsername(user.getUsername());
        Mockito.when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/notes-api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Your account was deleted successfully"));
    }

}