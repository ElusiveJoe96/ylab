package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.Role;

/**
 * Data Transfer Object (DTO) for representing a user.
 * <p>
 * This class is used to encapsulate the data related to a user, including their name, email,
 * password, role, contact information, and an optional authentication token.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * The name of the user.
     * <p>
     * This field represents the full name of the user.
     * </p>
     */
    private String name;

    /**
     * The email address of the user.
     * <p>
     * This is the user's email, which is used for authentication and communication.
     * </p>
     */
    private String email;

    /**
     * The password of the user.
     * <p>
     * This field holds the user's password for authentication purposes. Note: In a real-world
     * application, passwords should be securely hashed and not stored in plain text.
     * </p>
     */
    private String password;

    /**
     * The role assigned to the user.
     * <p>
     * This field indicates the user's role within the system, such as ADMIN, USER, etc.
     * It is represented by the {@link Role} enum.
     * </p>
     */
    private Role role;

    /**
     * Contact information for the user.
     * <p>
     * This field can include additional contact details for the user, such as a phone number.
     * </p>
     */
    private String contactInfo;

    /**
     * An authentication token for the user.
     * <p>
     * This optional field holds a token used for authenticating the user in subsequent requests.
     * It is typically provided upon successful login.
     * </p>
     */
    private String token;
}