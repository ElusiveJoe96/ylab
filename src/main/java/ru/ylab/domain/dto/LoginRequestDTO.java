package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing a login request.
 * <p>
 * This class is used to encapsulate the data required for user authentication
 * when a user attempts to log in to the system. It contains the user's email
 * address and password.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    /**
     * The email address of the user.
     * <p>
     * This is used as the primary identifier for the user during the login process.
     * It should be in a valid email format.
     * </p>
     */
    private String email;

    /**
     * The password of the user.
     * <p>
     * This is used in conjunction with the email address to authenticate the user.
     * It should be kept confidential and not exposed or logged.
     * </p>
     */
    private String password;
}
