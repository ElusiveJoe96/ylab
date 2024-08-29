package ru.ylab.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ylab.domain.dto.LoginRequestDTO;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    private UserDTO sampleUser;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);

        sampleUser = new UserDTO("John Doe", "john.doe@mail.com", "password123", null, "Some contact info", "token");
    }

    @Test
    @DisplayName("registerUser should return the created user")
    public void registerUser_ShouldReturnCreatedUser() {
        when(userService.registerUser(any(UserDTO.class))).thenReturn(sampleUser);

        ResponseEntity<UserDTO> response = userController.registerUser(sampleUser);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(sampleUser, response.getBody());
    }

    @Test
    @DisplayName("authenticateUser should return unauthorized when credentials are incorrect")
    public void authenticateUser_ShouldReturnUnauthorized_WhenCredentialsAreIncorrect() {
        when(userService.authenticateUser("john.doe@example.com", "wrongpassword")).thenReturn(Optional.empty());

        LoginRequestDTO loginRequest = new LoginRequestDTO("john.doe@mail.com", "wrongpassword");
        ResponseEntity<UserDTO> response = userController.authenticateUser(loginRequest);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("getAllUsers should return a list of users")
    public void getAllUsers_ShouldReturnUserList() {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(sampleUser));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(sampleUser, response.getBody().get(0));
    }

    @Test
    @DisplayName("getUserById should return user when user exists")
    public void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userService.getUserById(1)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(sampleUser, response.getBody());
    }

    @Test
    @DisplayName("getUserById should return not found when user does not exist")
    public void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("updateUser should return updated user")
    public void updateUser_ShouldReturnUpdatedUser() {
        when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(sampleUser);

        ResponseEntity<UserDTO> response = userController.updateUser(1, sampleUser);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(sampleUser, response.getBody());
    }

    @Test
    @DisplayName("deleteUser should return no content")
    public void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1);

        ResponseEntity<Void> response = userController.deleteUser(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1);
    }
}
