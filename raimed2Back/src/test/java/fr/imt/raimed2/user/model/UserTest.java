package fr.imt.raimed2.user.model;

import fr.imt.raimed2.diagnostic.model.Diagnostic;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getAuthorities() {
        User user = User.builder().role(Role.STUDENT).build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(Role.STUDENT.getAuthorities(), authorities);
    }

    @Test
    void getPassword() {
        String password = "password";
        User user = User.builder().password(password).build();
        assertEquals(password, user.getPassword());
    }

    @Test
    void getUsername() {
        String username = "username";
        User user = User.builder().username(username).build();
        assertEquals(username, user.getUsername());
    }

    @Test
    void isAccountNonExpired() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        User user = new User();
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        User user = new User();
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        User user = new User();
        assertTrue(user.isEnabled());
    }

    @Test
    void testEquals() {
        UUID id = UUID.randomUUID();
        User user1 = User.builder().id(id).build();
        User user2 = User.builder().id(id).build();
        assertEquals(user1, user2);
    }

    @Test
    void testHashCode() {
        UUID id = UUID.randomUUID();
        User user1 = User.builder().id(id).build();
        User user2 = User.builder().id(id).build();
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void getId() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).build();
        assertEquals(id, user.getId());
    }

    @Test
    void getFirstname() {
        String firstname = "John";
        User user = User.builder().firstname(firstname).build();
        assertEquals(firstname, user.getFirstname());
    }

    @Test
    void getLastname() {
        String lastname = "Doe";
        User user = User.builder().lastname(lastname).build();
        assertEquals(lastname, user.getLastname());
    }

    @Test
    void getEmail() {
        String email = "john.doe@example.com";
        User user = User.builder().email(email).build();
        assertEquals(email, user.getEmail());
    }

    @Test
    void getRole() {
        Role role = Role.TEACHER;
        User user = User.builder().role(role).build();
        assertEquals(role, user.getRole());
    }

    @Test
    void getDiagnostics() {
        Set<Diagnostic> diagnostics = Set.of(new Diagnostic());
        User user = User.builder().diagnostics(diagnostics).build();
        assertEquals(diagnostics, user.getDiagnostics());
    }

    @Test
    void setId() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void setUsername() {
        String username = "username";
        User user = new User();
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void setFirstname() {
        String firstname = "John";
        User user = new User();
        user.setFirstname(firstname);
        assertEquals(firstname, user.getFirstname());
    }

    @Test
    void setLastname() {
        String lastname = "Doe";
        User user = new User();
        user.setLastname(lastname);
        assertEquals(lastname, user.getLastname());
    }

    @Test
    void setEmail() {
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void setPassword() {
        String password = "password";
        User user = new User();
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void setRole() {
        Role role = Role.STUDENT;
        User user = new User();
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void setDiagnostics() {
        Set<Diagnostic> diagnostics = Set.of(new Diagnostic());
        User user = new User();
        user.setDiagnostics(diagnostics);
        assertEquals(diagnostics, user.getDiagnostics());
    }

    @Test
    void builder() {
        User user = User.builder().username("username").build();
        assertEquals("username", user.getUsername());
    }
}