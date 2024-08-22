package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.implementation.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserServiceImpl userService;

    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("Register user with invalid email and ensure no user is saved")
    public void testRegisterUser_InvalidEmail() throws Exception {
        UserDTO userDTO = new UserDTO("John Doe", "invalid-email", "password123", "123-456-7890");

        userService.registerUser(userDTO, response);

        verify(userRepository, never()).save(any(User.class));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Login with correct credentials and ensure user is authenticated and logged in")
    public void testLogin_Successful() throws Exception {
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login("john.doe@example.com", "password123", response);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        assertEquals(user, AuditService.loggedInUser);
        verify(auditService).logAction(eq(user.getId()), eq("LOGIN"), anyString());
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("Login with incorrect credentials and ensure login fails")
    public void testLogin_Failure() throws Exception {
        when(userRepository.findByEmail("wrong.email@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("wrong.email@example.com", "wrongpassword", response);

        assertFalse(result.isPresent());
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("Logout when user is logged in and ensure user is logged out")
    public void testLogout_Successful() throws Exception {
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        AuditService.loggedInUser = user;

        userService.logout(response);

        assertNull(AuditService.loggedInUser);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("Logout when no user is logged in and ensure no actions are performed")
    public void testLogout_NoUserLoggedIn() throws Exception {
        AuditService.loggedInUser = null;

        userService.logout(response);

        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("Delete user when user exists and ensure user is deleted")
    public void testDeleteUser() throws Exception {
        int userId = 1;
        User user = new User(userId, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId, response);

        verify(userRepository).delete(userId);
        verify(auditService).logAction(anyInt(), eq("DELETE_USER"), anyString());
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Delete user when user is not found and ensure no actions are performed")
    public void testDeleteUser_NotFound() throws Exception {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.deleteUser(userId, response);

        verify(userRepository, never()).delete(anyInt());
        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("View all users and ensure users are retrieved")
    public void testViewAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1, "John Doe", "john.doe@example.com",
                        "password123", Role.CLIENT, "123-456-7890"),
                new User(2, "Jane Smith", "jane.smith@example.com",
                        "password456", Role.ADMIN, "987-654-3210")
        );
        when(userRepository.findAll()).thenReturn(users);

        userService.viewAllUsers(response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("Update user role and ensure role is updated and saved")
    public void testUpdateUserRole() throws Exception {
        int userId = 1;
        User user = new User(1, "John Doe", "john.doe@example.com",
                "password123", Role.CLIENT, "123-456-7890");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUserRole(userId, Role.ADMIN, response);

        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
        verify(auditService).logAction(anyInt(), eq("UPDATE_USER_ROLE"), anyString());
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(anyString());
    }

    @Test
    @DisplayName("View information of the currently logged in user")
    public void testViewMyInfo() throws Exception {
        AuditService.loggedInUser = new User(1, "John Doe",
                "john.doe@example.com", "password123", Role.CLIENT, "123-456-7890");

        userService.viewMyInfo(response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
