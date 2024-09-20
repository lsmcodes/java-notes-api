package io.github.lsmcodes.notes_api.controller.authentication;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.lsmcodes.notes_api.dto.model.security.AuthenticationDTO;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.security.TokenService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link AuthenticationController} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private VerificationService verificationService;

    /**
     * Tests the
     * {@link AuthenticationController#login(AuthenticationDTO dto, BindingResult result)}
     * to ensure it correctly authenticates a user and generates a token.
     * 
     * @throws Exception if an error occurs during user authentication.
     */
    @Test
    @Order(1)
    @DisplayName("AuthenticationController login should authenticate user and generate a token")
    public void login_ShouldAuthenticateUserAndGenerateAToken() throws Exception {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        String token = "generated_token";

        AuthenticationDTO dto = new AuthenticationDTO(user.getUsername(), user.getPassword());
        var authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        Mockito.doNothing().when(this.verificationService).verifyIfUserExistsByUsername(user.getUsername());
        Mockito.when(this.authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(this.tokenService.generateToken((User) authentication.getPrincipal())).thenReturn(token);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/notes-api/login")
                .content(new ObjectMapper().writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value(token));

    }

}