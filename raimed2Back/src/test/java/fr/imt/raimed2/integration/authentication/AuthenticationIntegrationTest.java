package fr.imt.raimed2.integration.authentication;

import fr.imt.raimed2.integration.AbstractIntegrationTest;
import fr.imt.raimed2.security.dto.request.LoginDto;
import fr.imt.raimed2.security.dto.request.SignUpDto;
import fr.imt.raimed2.security.repository.RefreshTokenRepository;
import fr.imt.raimed2.user.model.Role;
import fr.imt.raimed2.user.model.User;
import fr.imt.raimed2.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
public class AuthenticationIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        userRepository.save(
            User.builder()
                .username("user1")
                .email("user1@raimed.fr")
                .firstname("user1")
                .lastname("user1")
                .password(passwordEncoder.encode("password"))
                .role(Role.STUDENT)
                .build()
        );
    }

    @AfterEach
    public void afterEach() {
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void given_valid_signupdto_should_registration_successful() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("user2");
        signUpDto.setFirstname("user2");
        signUpDto.setLastname("user2");
        signUpDto.setEmail("user2@raimed.fr");
        signUpDto.setPassword(passwordEncoder.encode("password"));
        mockMvc.perform(
                post("/api/v1/auth/signup")
                        .content(new ObjectMapper().writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("user2"))
                .andExpect(jsonPath("$.firstname").value("user2"))
                .andExpect(jsonPath("$.lastname").value("user2"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("user2@raimed.fr"));
    }

    @Test
    public void given_already_existing_login_should_registration_fail() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("user1");
        signUpDto.setFirstname("user1");
        signUpDto.setLastname("user1");
        signUpDto.setEmail("user1differentemail@raimed.fr");
        signUpDto.setPassword(passwordEncoder.encode("password"));
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .content(new ObjectMapper().writeValueAsString(signUpDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void given_already_existing_email_should_registration_fail() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("user2");
        signUpDto.setFirstname("user2");
        signUpDto.setLastname("user2");
        signUpDto.setEmail("user1@raimed.fr");
        signUpDto.setPassword(passwordEncoder.encode("password"));
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .content(new ObjectMapper().writeValueAsString(signUpDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void given_valid_credentials_should_authentication_successful() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user1");
        loginDto.setPassword("password");
        mockMvc.perform(
                post("/api/v1/auth/login")
                        .content(new ObjectMapper().writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refreshToken"));
    }

    @Test
    public void given_false_credentials_should_authentication_failed() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user1");
        loginDto.setPassword("falsePassword");
        mockMvc.perform(
            post("/api/v1/auth/login")
                    .content(new ObjectMapper().writeValueAsString(loginDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.accessToken").doesNotExist())
                .andExpect(content().string("Bad credentials"))
                .andExpect(cookie().doesNotExist("refreshToken"));
    }

}
