package fr.imt.raimed2.security.service;

import fr.imt.raimed2.user.model.User;
import fr.imt.raimed2.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Raimed2UserDetailsServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final Raimed2UserDetailsService userDetailsService = new Raimed2UserDetailsService(userRepository);

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        User user = new User();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("existingUser");

        assertEquals(user, result);
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonExistingUser"));
    }

    @Test
    void loadUserByUsernameHandlesNullUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsernameHandlesEmptyUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(""));
    }
}