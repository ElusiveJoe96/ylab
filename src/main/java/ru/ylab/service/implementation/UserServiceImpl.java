package ru.ylab.service.implementation;

import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.dto.mapper.UserMapper;
import ru.ylab.domain.model.User;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.UserService;
import ru.ylab.util.ResourceNotFoundException;
import ru.ylab.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        ValidationUtil.validateUserDTO(userDTO);
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public Optional<UserDTO> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return Optional.of(userMapper.toDTO(userOpt.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(int id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    @Override
    public UserDTO updateUser(int id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));

        userMapper.updateEntityFromDTO(userDTO, existingUser);
        userRepository.save(existingUser);
        return userMapper.toDTO(existingUser);
    }

    @Override
    public void deleteUser(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.delete(id);
        } else {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
    }

}
