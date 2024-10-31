package fr.imt.raimed2.security.service;

import fr.imt.raimed2.security.dto.AuthenticationJwts;
import fr.imt.raimed2.security.dto.request.LoginDto;
import fr.imt.raimed2.security.dto.request.SignUpDto;
import fr.imt.raimed2.security.model.RefreshToken;
import fr.imt.raimed2.security.repository.RefreshTokenRepository;
import fr.imt.raimed2.user.exception.EmailAlreadyInUseException;
import fr.imt.raimed2.user.exception.UsernameAlreadyInUseException;
import fr.imt.raimed2.user.model.Role;
import fr.imt.raimed2.user.model.User;
import fr.imt.raimed2.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        authenticationManager = mock(AuthenticationManager.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        response = mock(HttpServletResponse.class);
        authenticationService = new AuthenticationService(userRepository, refreshTokenRepository, authenticationManager, passwordEncoder, jwtService);
    }

    @Test
    void registerCreatesNewUser() throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("username");
        signUpDto.setFirstname("firstname");
        signUpDto.setLastname("lastname");
        signUpDto.setEmail("email@example.com");
        signUpDto.setPassword("password");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .username(signUpDto.getUsername())
                .firstname(signUpDto.getFirstname())
                .lastname(signUpDto.getLastname())
                .email(signUpDto.getEmail())
                .password("encodedPassword")
                .role(Role.STUDENT)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User user = authenticationService.register(signUpDto);

        assertNotNull(user);
        assertEquals(signUpDto.getUsername(), user.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerThrowsExceptionWhenUsernameAlreadyInUse() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("username");
        signUpDto.setFirstname("firstname");
        signUpDto.setLastname("lastname");
        signUpDto.setEmail("email@example.com");
        signUpDto.setPassword("password");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyInUseException.class, () -> authenticationService.register(signUpDto));
    }

    @Test
    void registerThrowsExceptionWhenEmailAlreadyInUse() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("username");
        signUpDto.setFirstname("firstname");
        signUpDto.setLastname("lastname");
        signUpDto.setEmail("email@example.com");
        signUpDto.setPassword("password");

        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> authenticationService.register(signUpDto));
    }

    @Test
    void loginReturnsAuthenticationJwts() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("username");
        loginDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUsername("username");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationJwts jwts = authenticationService.login(loginDto);

        assertNotNull(jwts);
        assertEquals("accessToken", jwts.getAccessToken());
        assertEquals("refreshToken", jwts.getRefreshToken());
    }

    @Test
    void refreshReturnsNewTokens() {
        String refreshToken = "refreshToken";
        User user = new User();
        user.setUsername("username");
        RefreshToken savedRefreshToken = new RefreshToken();
        savedRefreshToken.setToken(refreshToken);

        when(jwtService.extractUsername(refreshToken)).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValidForUser(refreshToken, user)).thenReturn(true);
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(savedRefreshToken));
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        AuthenticationJwts jwts = authenticationService.refresh(refreshToken);

        assertNotNull(jwts);
        assertEquals("newAccessToken", jwts.getAccessToken());
        assertEquals("newRefreshToken", jwts.getRefreshToken());
    }

    @Test
    void refreshThrowsExceptionForInvalidRefreshToken() {
        String refreshToken = "refreshToken";
        User user = new User();
        user.setUsername("username");

        when(jwtService.extractUsername(refreshToken)).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValidForUser(refreshToken, user)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authenticationService.refresh(refreshToken));
    }

    @Test
    void logoutRevokesUserRefreshToken() {
        User user = new User();
        authenticationService.logout(user);
        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    void generateRefreshTokenCookieSetsCookie() {
        String refreshToken = "refreshToken";
        authenticationService.generateRefreshTokenCookie(response, refreshToken);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void deleteRefreshTokenCookieRemovesCookie() {
        authenticationService.deleteRefreshTokenCookie(response);
        verify(response).addCookie(any(Cookie.class));
    }
}