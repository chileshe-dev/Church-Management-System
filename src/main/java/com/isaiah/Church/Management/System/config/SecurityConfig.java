package com.isaiah.Church.Management.System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // ==================================
                // PUBLIC PAGES
                // ==================================

                .requestMatchers(
    "/",
    "/login.html",
    "/register.html",

    "/member-login.html",
    "/member-dashboard.html",
    "/member-contributions.html",
    "/member-attendance.html",
     "/member-events.html",

    "/member-registration",
    "/member-login",
    "/member-login/logout",
    "/member-account/me",
    "/member-contributions",
    "/member-payments",
    "/member-events",
    "/member-attendance",


    "/css/**",
    "/js/**",
    "/favicon.ico"
)
.permitAll()

                // ==================================
                // MEMBER DASHBOARD ENTRY POINT
                // ==================================

                .requestMatchers("/member-dashboard")
                .permitAll()

                // ==================================
                // STAFF LOGIN
                // ==================================

                .requestMatchers("/login")
                .permitAll()

                // ==================================
                // EVERYTHING ELSE
                // ==================================

                .anyRequest()
                .authenticated()
            )

            // ==================================
            // STAFF LOGIN
            // ==================================

            .formLogin(form -> form

                .loginPage("/login.html")

                .loginProcessingUrl("/login")

                .defaultSuccessUrl(
                    "/dashboard.html",
                    true
                )

                .failureUrl(
                    "/login.html?error=true"
                )

                .permitAll()
            )

            // ==================================
            // STAFF LOGOUT
            // ==================================

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login.html")

                .permitAll()
            );

        return http.build();
    }
}