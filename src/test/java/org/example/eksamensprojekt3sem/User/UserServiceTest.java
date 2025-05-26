package org.example.eksamensprojekt3sem.User;

import org.example.eksamensprojekt3sem.Enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    //Hent alle brugere
    @Test
    void shouldReturnAllUsers() {
        User u1 = new User();
        User u2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    //Hent bruger med gyldigt ID
    @Test
    void shouldReturnUserById() {
        User u = new User();
        u.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
    }

    //Håndtér ugyldigt ID korrekt
    @Test
    void shouldThrowWhenGettingUserWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(0));
    }

    //Tilføj ny bruger
    @Test
    void shouldAddUser() {
        User u = new User();
        u.setUsername("john");
        u.setUserRole(UserRole.TRAINER);
        when(userRepository.save(u)).thenReturn(u);

        User saved = userService.addUser(u);

        assertEquals("john", saved.getUsername());
        assertEquals(UserRole.TRAINER, saved.getUserRole());
    }

    //Opdater bruger korrekt
    @Test
    void shouldUpdateUser() {
        User existing = new User();
        existing.setUserId(1L);
        existing.setUsername("oldUser");
        existing.setPassword("1234");
        existing.setUserRole(UserRole.CHAIRMAN);

        User updated = new User();
        updated.setUsername("newUser");
        updated.setPassword("abcd");
        updated.setUserRole(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        Optional<User> result = userService.updateUser(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("newUser", result.get().getUsername());
        assertEquals(UserRole.ADMIN, result.get().getUserRole());
    }

    //Returnér false hvis bruger ikke findes
    @Test
    void shouldThrowWhenUpdatingWithInvalidId() {
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(0, user));
    }

    //Slet bruger hvis ID findes
    @Test
    void shouldDeleteExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteNonExistingUser() {
        when(userRepository.existsById(99L)).thenReturn(false);

        boolean result = userService.deleteUser(99L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }

    //Kast exception ved ugyldigt ID
    @Test
    void shouldThrowWhenDeletingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(0));
    }
}
