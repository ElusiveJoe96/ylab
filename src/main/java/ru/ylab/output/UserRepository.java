package ru.ylab.output;

import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepository {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    public void save(User user) {
        if (user.getId() == 0) {
            user.setId(idCounter++);
        }
        users.put(user.getId(), user);
    }

    public void delete(int userId) {
        users.remove(userId);
    }

    public Optional<User> findById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public  Optional<User> findByEmail(String email) {
        return users.values().stream().filter((user -> user.getEmail().equals(email))).findFirst();
    }

    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    public List<User> findByRole(Role role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

}
