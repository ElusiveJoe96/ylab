package ru.ylab.service;

import ru.ylab.domain.dto.UserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing users.
 * Provides methods for registering, authenticating, retrieving, updating, and deleting users.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param userDTO the user to be registered, represented as {@link UserDTO}.
     * @return the registered user as {@link UserDTO}.
     */
    UserDTO registerUser(UserDTO userDTO);

    /**
     * Authenticates a user based on their email and password.
     *
     * @param email the email of the user attempting to authenticate.
     * @param password the password of the user attempting to authenticate.
     * @return an {@link Optional} containing the authenticated user as {@link UserDTO},
     *         or {@link Optional#empty()} if authentication fails.
     */
    Optional<UserDTO> authenticateUser(String email, String password);

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users as {@link UserDTO}.
     */
    List<UserDTO> getAllUsers();

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return an {@link Optional} containing the user as {@link UserDTO}, or {@link Optional#empty()} if no user is found with the given ID.
     */
    Optional<UserDTO> getUserById(int id);

    /**
     * Updates an existing user in the system.
     *
     * @param id the unique identifier of the user to be updated.
     * @param userDTO the updated user data, represented as {@link UserDTO}.
     * @return the updated user as {@link UserDTO}, or {@code null} if no user is found with the given ID.
     */
    UserDTO updateUser(int id, UserDTO userDTO);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted.
     */
    void deleteUser(int id);
}
