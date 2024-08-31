package io.github.lsmcodes.notes_api.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.lsmcodes.notes_api.service.security.SecurityService;
import io.github.lsmcodes.notes_api.service.security.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom filter that extends {@link OncePerRequestFilter} and manages user
 * authentication.
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityService securityService;

    /**
     * Filters the request and response and passes them along the filter chain.
     * 
     * @param request     The {@link HttpServletRequest} object that contains the
     *                    client's request.
     * @param response    The {@link HttpServletResponse} object that contains the
     *                    client's response.
     * @param filterChain The {@link FilterChain} used to pass the request and
     *                    response to the next entity in the chain.
     * @throws ServletException if an error occurs during filtering.
     * @throws IOException      if an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        try {
            if (token != null) {
                token = token.replace("Bearer ", "");

                String username = this.tokenService.getSubjectFromToken(token);
                UserDetails user = this.securityService.loadUserByUsername(username);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                | AuthenticationException e) {
            logger.error("Authentication error: {}", e.getLocalizedMessage());
        }

        filterChain.doFilter(request, response);

    }

}