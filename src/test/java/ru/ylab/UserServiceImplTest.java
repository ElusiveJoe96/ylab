package ru.ylab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.implementation.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        AuditService.loggedInUser = null;
    }

//    @Test
//    public void testRegisterUserSuccess() {
//        String name = "John Doe";
//        String email = "john.doe@example.com";
//        String password = "password";
//        Role role = Role.CLIENT;
//        String contactInfo = "123-456-7890";
//
//        userServiceImpl.registerUser(name, email, password, role, contactInfo);
//
//        User user = new User(0, name, email, password, role, contactInfo);
//        verify(userRepository, times(1)).save(user);
//        verify(auditService, times(1)).logAction(anyInt(), eq("REGISTER_USER"), anyString());
//    }

//    @Test
//    public void testRegisterUserInvalidEmail() {
//        String name = "John Doe";
//        String email = "invalid-email";
//        String password = "password";
//        Role role = Role.CLIENT;
//        String contactInfo = "123-456-7890";
//
//        userServiceImpl.registerUser(name, email, password, role, contactInfo);
//
//        verify(userRepository, never()).save(any(User.class));
//        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
//    }

//    @Test
//    public void testLoginSuccess() {
//        String email = "john.doe@example.com";
//        String password = "password";
//        User user = new User(1, "John Doe", email, password, Role.CLIENT, "123-456-7890");
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//
//        Optional<User> loggedInUser = userServiceImpl.login(email, password);
//
//        verify(auditService, times(1)).logAction(user.getId(), "LOGIN",
//                "User logged in: " + email);
//        assertTrue(loggedInUser.isPresent());
//        assertEquals(user, loggedInUser.get());
//    }

//    @Test
//    public void testLoginFailure() {
//        String email = "john.doe@example.com";
//        String password = "wrongpassword";
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        Optional<User> loggedInUser = userServiceImpl.login(email, password);
//
//        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
//        assertFalse(loggedInUser.isPresent());
//    }

//    @Test
//    public void testUpdateUserSuccess() {
//        int userId = 1;
//        String name = "Jane Doe";
//        String email = "jane.doe@example.com";
//        String password = "newpassword";
//        Role role = Role.ADMIN;
//        String contactInfo = "987-654-3210";
//        User existingUser = new User(userId, "John Doe", "john.doe@example.com",
//                "password", Role.CLIENT, "123-456-7890");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        AuditService.loggedInUser = existingUser;
//        userServiceImpl.updateUser(userId, name, email, password, role, contactInfo);
//
//        verify(userRepository, times(1)).save(existingUser);
//        verify(auditService, times(1)).logAction(anyInt(), eq("UPDATE_USER"), anyString());
//        assertEquals(name, existingUser.getName());
//        assertEquals(email, existingUser.getEmail());
//        assertEquals(password, existingUser.getPassword());
//        assertEquals(role, existingUser.getRole());
//        assertEquals(contactInfo, existingUser.getContactInfo());
//    }

    @Test
    public void testDeleteUserSuccess() {
        int userId = 1;
        User user = new User(userId, "John Doe", "john.doe@example.com",
                "password", Role.CLIENT, "123-456-7890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        AuditService.loggedInUser = user;

        userServiceImpl.deleteUser(userId);

        verify(userRepository, times(1)).delete(userId);
        verify(auditService, times(1)).logAction(anyInt(), eq("DELETE_USER"),
                eq("Deleted user with ID: " + userId));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1, "John Doe", "john.doe@example.com",
                "password", Role.CLIENT, "123-456-7890");
        User user2 = new User(2, "Jane Doe", "jane.doe@example.com",
                "password", Role.ADMIN, "987-654-3210");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        userServiceImpl.getAllUsers();

        verify(userRepository, times(1)).findAll();
    }

//    @Test
//    public void testUpdateUserRole() {
//        int userId = 1;
//        Role newRole = Role.ADMIN;
//        User user = new User(userId, "John Doe", "john.doe@example.com",
//                "password", Role.CLIENT, "123-456-7890");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        AuditService.loggedInUser = user;
//        userServiceImpl.updateUserRole(userId, newRole);
//
//        verify(userRepository, times(1)).save(user);
//        verify(auditService, times(1)).logAction(anyInt(),
//                eq("UPDATE_USER_ROLE"), eq("Updated role for user ID: " + userId + " to " + newRole));
//        assertEquals(newRole, user.getRole());
//    }

    @Test
    public void testViewMyInfo() {
        AuditService.loggedInUser = new User(1, "John Doe", "john.doe@example.com",
                "password", Role.CLIENT, "123-456-7890");

        userServiceImpl.viewMyInfo();
    }

    @Test
    public void testGetUserById() {
        int userId = 1;
        User user = new User(userId, "John Doe", "john.doe@example.com",
                "password", Role.CLIENT, "123-456-7890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceImpl.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testGetUserByEmail() {
        String email = "john.doe@example.com";
        User user = new User(1, "John Doe", email, "password",
                Role.CLIENT, "123-456-7890");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceImpl.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testGetUsersByRole() {
        Role role = Role.CLIENT;
        User user1 = new User(1, "John Doe", "john.doe@example.com", "password",
                role, "123-456-7890");
        User user2 = new User(2, "Jane Doe", "jane.doe@example.com", "password",
                role, "987-654-3210");

        when(userRepository.findByRole(role)).thenReturn(List.of(user1, user2));

        List<User> users = userServiceImpl.getUsersByRole(role);

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }
}
