package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.implementation.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        Scanner scanner = new Scanner("John Doe\ninvalid-email\npassword123\n123-456-7890");

        userService.registerUser(scanner);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLogin_Successful() {
        Scanner scanner = new Scanner("john.doe@example.com\npassword123");
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        AuditService.loggedInUser = user;
        Optional<User> result = userService.login(scanner);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        assertEquals(user, AuditService.loggedInUser);
        verify(auditService).logAction(eq(user.getId()), eq("LOGIN"), anyString());
    }

    @Test
    public void testLogin_Failure() {
        Scanner scanner = new Scanner("wrong.email@example.com\nwrongpassword");
        when(userRepository.findByEmail("wrong.email@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.login(scanner);

        assertFalse(result.isPresent());
    }

    @Test
    public void testLogout_Successful() {
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        AuditService.loggedInUser = user;

        userService.logout();

        assertNull(AuditService.loggedInUser);
        verify(auditService).logAction(eq(user.getId()), eq("LOGOUT"), anyString());
    }

    @Test
    public void testLogout_NoUserLoggedIn() {
        AuditService.loggedInUser = null;

        userService.logout();

        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;
        User user = new User(userId, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).delete(userId);
        verify(auditService).logAction(anyInt(), eq("DELETE_USER"), anyString());
    }

    @Test
    public void testDeleteUser_NotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.deleteUser(userId);

        verify(userRepository, never()).delete(anyInt());
        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
    }

    @Test
    public void testViewAllUsers() {
        List<User> users = Arrays.asList(
                new User(1, "John Doe", "john.doe@example.com",
                        "password123", Role.CLIENT, "123-456-7890"),
                new User(2, "Jane Smith", "jane.smith@example.com",
                        "password456", Role.ADMIN, "987-654-3210")
        );
        when(userRepository.findAll()).thenReturn(users);

        userService.viewAllUsers();
    }

    @Test
    public void testUpdateUserRole() {
        Scanner scanner = new Scanner("1\nADMIN");
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        AuditService.loggedInUser = user;
        userService.updateUserRole(scanner);

        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
        verify(auditService).logAction(anyInt(), eq("UPDATE_USER_ROLE"), anyString());
    }

    @Test
    public void testViewMyInfo() {
        AuditService.loggedInUser = new User(1, "John Doe",
                "john.doe@example.com", "password123", Role.CLIENT, "123-456-7890");
        userService.viewMyInfo();
    }
}
