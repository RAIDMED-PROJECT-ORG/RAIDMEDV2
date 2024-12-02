package fr.imt.raimed2.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.imt.raimed2.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private ObjectMapper objectMapper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        objectMapper = mock(ObjectMapper.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        jwtAuthFilter = new JwtAuthFilter(jwtService, userDetailsService, objectMapper);
    }

    @Test
    void doFilterInternalAllowsAuthRequests() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalReturnsBadRequestForMissingAuthorizationHeader() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/some/other/endpoint");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void doFilterInternalReturnsUnauthorizedForExpiredToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/some/other/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredToken");
        when(jwtService.extractUsername("expiredToken")).thenThrow(ExpiredJwtException.class);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void doFilterInternalReturnsUnauthorizedForInvalidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/some/other/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenThrow(SignatureException.class);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void doFilterInternalSetsAuthenticationForValidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/some/other/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("username");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtService.isTokenValidForUser("validToken", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}