package com.example.Userservice.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    public JWTFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // Check if the Authorization header is missing or doesn't start with "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        // 🚨 Check if the token is invalidated
        if (jwtUtil.isTokenInvalid(token)) {
            logger.warn("Token is invalidated: {}", token); // Log the invalid token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is invalidated. Please log in again.");
            return;
        }

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();
            List<String> authorities = claims.get("authorities", List.class);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Convert the authorities list from the JWT to GrantedAuthority objects
                List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Log the extracted authorities for debugging
                logger.debug("Authorities extracted for user {}: {}", username, grantedAuthorities);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            logger.error("Error extracting claims from JWT: ", e); // Log the error if exception occurs
            SecurityContextHolder.clearContext(); // Clear context if any error occurs
        }

        // Continue with the request filtering process
        filterChain.doFilter(request, response);
    }
}
