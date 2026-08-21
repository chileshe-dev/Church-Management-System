package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.User;
import com.isaiah.Church.Management.System.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.isaiah.Church.Management.System.dto.LoginResponse;


import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            BCryptPasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUser(Integer id, User updatedUser) {

    User user = repository.findById(id).orElse(null);

    if (user != null) {

        user.setUsername(updatedUser.getUsername());

        user.setPassword(
          passwordEncoder.encode(updatedUser.getPassword())
         );
        user.setRole(updatedUser.getRole());

        return repository.save(user);

    }

    return null;

}

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteUser(Integer id) {
        repository.deleteById(id);
    }

    public LoginResponse login(String username, String password) {

    User user = repository.findByUsername(username).orElse(null);

    if (user != null &&
    passwordEncoder.matches(password, user.getPassword())) {

    return new LoginResponse(
            user.getUserId(),
            user.getUsername(),
            user.getRole()
    );

  }

return null;
  }


public User saveUser(User user) {

    user.setPassword(
            passwordEncoder.encode(user.getPassword())
    );

    return repository.save(user);

  }

public User findByUsername(String username) {

    return repository.findByUsername(username)
            .orElse(null);

  }

}