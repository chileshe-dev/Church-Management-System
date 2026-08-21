package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.dto.CurrentUserResponse;
import com.isaiah.Church.Management.System.model.User;
import com.isaiah.Church.Management.System.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private final UserRepository repository;

    public UserInfoController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/me")
    public CurrentUserResponse currentUser(Authentication authentication) {

        User user = repository
                .findByUsername(authentication.getName())
                .orElseThrow();

        return new CurrentUserResponse(

                user.getUsername(),

                user.getRole().name()

        );
    }
}