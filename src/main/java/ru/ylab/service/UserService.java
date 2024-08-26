package ru.ylab.service;

import ru.ylab.domain.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    Optional<UserDTO> authenticateUser(String email, String password);
    List<UserDTO> getAllUsers();
    Optional<UserDTO> getUserById(int id);
    UserDTO updateUser(int id, UserDTO userDTO);
    void deleteUser(int id);
}
