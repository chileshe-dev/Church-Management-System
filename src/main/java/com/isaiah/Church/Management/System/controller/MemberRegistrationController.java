package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.MemberAccount;
import com.isaiah.Church.Management.System.service.MemberRegistrationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-registration")
public class MemberRegistrationController {

    private final MemberRegistrationService service;

    public MemberRegistrationController(
            MemberRegistrationService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> register(
            @RequestBody RegistrationRequest request) {

        try {

            MemberAccount account =
                    service.register(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPhone(),
                            request.getAddress(),
                            request.getUsername(),
                            request.getPassword()
                    );

            return ResponseEntity.ok(account);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    public static class RegistrationRequest {

        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String username;
        private String password;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}