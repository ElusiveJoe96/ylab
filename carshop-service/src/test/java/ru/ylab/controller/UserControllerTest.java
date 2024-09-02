package ru.ylab.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.service.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final UserDTO sampleUser = new UserDTO("John Doe", "john.doe@mail.com", "password123", null, "Some contact info", "token");

    @Test
    @DisplayName("registerUser should return the created user")
    public void registerUser_ShouldReturnCreatedUser() throws Exception {
        Mockito.when(userService.registerUser(any(UserDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@mail.com\", \"password\": \"password123\", \"contactInfo\": \"Some contact info\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(sampleUser.getName()));
    }

    @Test
    @DisplayName("getAllUsers should return a list of users")
    public void getAllUsers_ShouldReturnUserList() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(sampleUser));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(sampleUser.getName()));
    }

    @Test
    @DisplayName("getUserById should return user when user exists")
    public void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(Optional.of(sampleUser));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sampleUser.getName()));
    }

    @Test
    @DisplayName("getUserById should return not found when user does not exist")
    public void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateUser should return updated user")
    public void updateUser_ShouldReturnUpdatedUser() throws Exception {
        Mockito.when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@mail.com\", \"password\": \"password123\", \"contactInfo\": \"Updated contact info\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sampleUser.getName()));
    }

    @Test
    @DisplayName("deleteUser should return no content")
    public void deleteUser_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
