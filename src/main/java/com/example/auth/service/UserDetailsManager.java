package com.example.auth.service;

import com.example.auth.service.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsManager extends UserDetailsService {
    void createUser(UserDetails user);
    void updateUser(UserDetails user);
    void deleteUser(String username);

    void changePassword(String oldPassword, String newPassword);
    boolean userExists(String username);
}
