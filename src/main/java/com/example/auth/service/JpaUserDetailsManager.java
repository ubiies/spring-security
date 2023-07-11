package com.example.auth.service;


import com.example.auth.Entity.CustomUserDetails;
import com.example.auth.Entity.UserEntity;
import com.example.auth.Entity.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
// UserDetailsManager 의 구현체로 만들면
// Spring security Filter 에서 사용자 정보 회수에 활용할 수 있다.
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        createUser(CustomUserDetails.builder()
                .username("user")
                .password(passwordEncoder.encode("asdf"))
                .email("user@gmail.com")
                .build()
        );
    }

    // UserDetailsService.loadUserByUsername(String)
    // 실제로 Spring Security 내부에서 사용하는 반드시 구현해야 기대할 수 있는 메소드
    // loadUserByUsername -> 반드시 구현해야 정상적으로 동작한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity>  optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    // 새로운 사용자를 저장하는 메소드(선택)
    @Override
    public void createUser(UserDetails user) {
        log.info("try create user: {}", user.getUsername());
        // 사용자가 있으면 생성할 수 없다.
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            this.userRepository.save(
                    ((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 계정 이름을 가진 사용자가 존재하는지 확인하는 메소드(선택)
    @Override
    public boolean userExists(String username) {
        log.info("check id user: {} exists");
        return this.userRepository.existsByUsername(username);
    }

    // 얘네는 나중에 해보세요..
    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

}
