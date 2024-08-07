package ru.ylab.domain.model;

import lombok.Data;
import ru.ylab.domain.enums.Role;

@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String contactInfo;

    public User(int id, String name, String email, String password, Role role, String contactInfo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactInfo = contactInfo;
    }

}