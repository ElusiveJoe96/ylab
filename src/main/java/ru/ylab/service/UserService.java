package ru.ylab.service;

import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserDTO userDTO, HttpServletResponse resp);
    void logout(HttpServletResponse resp);
    void updateUser(User user, HttpServletResponse resp);
    Optional<User> login(String email, String password, HttpServletResponse resp);
    void deleteUser(int userId, HttpServletResponse resp);
    List<User> viewAllUsers(HttpServletResponse resp);
    void updateUserRole(int userId, Role newRole, HttpServletResponse resp);
    Optional<User> viewMyInfo(HttpServletResponse resp);
    Optional<User> getUserById(int userId, HttpServletResponse resp);

}
