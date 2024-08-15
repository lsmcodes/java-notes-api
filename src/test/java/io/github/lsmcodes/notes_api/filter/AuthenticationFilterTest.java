package io.github.lsmcodes.notes_api.filter;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Unit tests for the {@link AuthenticationFilter} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    /**
     * Tests {@link AuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)} doFilter method to ensure it correctly
     * passes request and response to {@link FilterChain#doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse)} method.
     * 
     * @throws ServletException if an error occurs during filtering.
     * @throws IOException      if an I/O error occurs during filtering.
     */
    @Test
    @DisplayName("AuthenticationFilter doFilterInternal should pass request and response to FilterChain")
    public void doFilterInternal_ShouldPassRequestAndResponseToFilterChain() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        OncePerRequestFilter filter = new AuthenticationFilter();

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        Mockito.verify(filterChain).doFilter(request, response);
    }

}