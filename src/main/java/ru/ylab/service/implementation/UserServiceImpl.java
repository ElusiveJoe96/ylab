package ru.ylab.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.UserService;
import ru.ylab.util.ValidationUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    private boolean checkAdminRole(HttpServletResponse resp) {
        if (AuditService.loggedInUser == null || !AuditService.loggedInUser.getRole().equals(Role.ADMIN)) {
            try {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write(objectMapper.writeValueAsString("Unauthorized action"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    public void registerUser(UserDTO userDTO, HttpServletResponse resp) {
        try {
            if (!ValidationUtil.isValidEmail(userDTO.getEmail())) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(objectMapper.writeValueAsString("Invalid email format"));
                return;
            }
            if (!ValidationUtil.isNonEmpty(userDTO.getName()) || !ValidationUtil.isNonEmpty(userDTO.getPassword())) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(objectMapper.writeValueAsString("Name and password cannot be empty"));
                return;
            }

            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(Role.CLIENT);
            user.setContactInfo(userDTO.getContactInfo());

            userRepository.save(user);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    public Optional<User> login(String email, String password, HttpServletResponse resp) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    AuditService.loggedInUser = user;
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(objectMapper.writeValueAsString(user));
                    return Optional.of(user);
                }
            }
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(objectMapper.writeValueAsString("Invalid email or password"));
            return Optional.empty();
        } catch (Exception e) {
            handleError(resp, e);
            return Optional.empty();
        }
    }

    @Override
    public void logout(HttpServletResponse resp) {
        try {
            if (AuditService.loggedInUser != null) {
                AuditService.loggedInUser = null;
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString("Logout successful"));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(objectMapper.writeValueAsString("No user is currently logged in"));
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    public void updateUser(User user, HttpServletResponse resp) {
        try {
            if (!checkAdminRole(resp)) return;

            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {
                userRepository.save(user);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(user));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(objectMapper.writeValueAsString("User not found"));
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    public void deleteUser(int userId, HttpServletResponse resp) {
        try {
            if (!checkAdminRole(resp)) return;

            Optional<User> userToDelete = userRepository.findById(userId);
            if (userToDelete.isPresent()) {
                userRepository.delete(userId);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(objectMapper.writeValueAsString("User not found"));
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    public List<User> viewAllUsers(HttpServletResponse resp) {
        try {
            if (!checkAdminRole(resp)) return Collections.emptyList();

            List<User> users = userRepository.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(users));
            return users;
        } catch (Exception e) {
            handleError(resp, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void updateUserRole(int userId, Role newRole, HttpServletResponse resp) {
        try {
            if (!checkAdminRole(resp)) return;

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setRole(newRole);
                userRepository.save(user);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(user));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(objectMapper.writeValueAsString("User not found"));
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    public Optional<User> viewMyInfo(HttpServletResponse resp) {
        try {
            if (AuditService.loggedInUser == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(objectMapper.writeValueAsString("User not logged in"));
                return Optional.empty();
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(AuditService.loggedInUser));
            return Optional.of(AuditService.loggedInUser);
        } catch (Exception e) {
            handleError(resp, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(int userId, HttpServletResponse resp) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(user.get()));
                return user;
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(objectMapper.writeValueAsString("User not found"));
                return Optional.empty();
            }
        } catch (Exception e) {
            handleError(resp, e);
            return Optional.empty();
        }
    }

    private void handleError(HttpServletResponse resp, Exception e) {
        try {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString("Error: " + e.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
