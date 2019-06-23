package com.differz.bc.web.user;

import com.differz.bc.core.User;
import com.differz.bc.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserDto);
    }

    public Optional<User> findUserIfExists(LoginInputDto loginInputDto) {
        String credentials = getEncodedCredentials(loginInputDto.getUsername(), loginInputDto.getPassword());
        return userRepository.findByCredentials(credentials);
    }

    public User registerUser(LoginInputDto loginInputDto) {
        String username = loginInputDto.getUsername();
        String password = loginInputDto.getPassword();
        String credentials = getEncodedCredentials(username, password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCredentials(credentials);
        userRepository.save(user);
        return user;
    }

    public String getEncodedCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public User getUserByIdOrThrow(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("can't get user id " + userId));
    }
}
