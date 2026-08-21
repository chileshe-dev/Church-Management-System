package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.User;
import com.isaiah.Church.Management.System.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

   @PostMapping
@PreAuthorize("hasRole('ADMIN')")
public User addUser(@RequestBody User user) {
    return service.saveUser(user);
}

   @GetMapping
@PreAuthorize("hasRole('ADMIN')")
public List<User> getAllUsers() {
    return service.getAllUsers();
}

   @GetMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public User getUser(@PathVariable Integer id) {
    return service.getUserById(id);
}

    @PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public User updateUser(
        @PathVariable Integer id,
        @RequestBody User user) {

    return service.updateUser(id, user);
}

    @DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(@PathVariable Integer id) {
    service.deleteUser(id);
}


   @GetMapping("/me")
public User getCurrentUser(Authentication authentication) {

    if (authentication == null) {
        return null;
    }

    return service.findByUsername(authentication.getName());
}

}
