package com.example.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    // Spring Security 내부에서 사용자 인증 과정에서 활용하는 메소드
    // 따라서 .. 반드시 정상 동작해야된다.
    UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException;
}
