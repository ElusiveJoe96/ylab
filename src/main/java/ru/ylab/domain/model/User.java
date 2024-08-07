package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ylab.domain.enums.Role;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String contactInfo;
}