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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/",
                    "/login.html",
                    "/css/**",
                    "/js/**",
                    "/favicon.ico"
                ).permitAll()

                .requestMatchers("/login")
                .permitAll()

                .anyRequest()
                .authenticated()
            )

            .formLogin(form -> form

                .loginPage("/login.html")

                .loginProcessingUrl("/login")

                .defaultSuccessUrl("/dashboard.html", true)

                .failureUrl("/login.html?error=true")

                .permitAll()
            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login.html")

                .permitAll()
            );

        return http.build();
    }
}