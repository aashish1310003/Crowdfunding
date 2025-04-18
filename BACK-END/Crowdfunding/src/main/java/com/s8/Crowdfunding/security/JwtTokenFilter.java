package com.s8.Crowdfunding.security;

import com.s8.Crowdfunding.webtoken.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JwtTokenFilter.class.getName());

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            LOGGER.info("Extracted Token: " + token);

            if (token != null) {
                String username = jwtService.extractUsername(token);
                LOGGER.info("Extracted Username: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                boolean isValid = jwtService.validateToken(token, userDetails);
                LOGGER.info("Is Token Valid: " + isValid);

                if (isValid) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    LOGGER.info("Authentication set in SecurityContextHolder for user: " + username);
                }
            }
        } catch (ExpiredJwtException e) {
            LOGGER.warning("JWT Token has expired: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
        } catch (MalformedJwtException | SignatureException e) {
            LOGGER.warning("Invalid JWT Token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        } catch (JwtException e) {
            LOGGER.warning("JWT processing error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Processing Error");
        } catch (Exception e) {
            LOGGER.severe("Unexpected error in JWT processing: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
