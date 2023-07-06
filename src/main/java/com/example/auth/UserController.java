package com.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller // 로그인 페이지를 보여주기 위해 Controller 사용
@RequestMapping("/users")
public class UserController {

    // 1. login 페이지로 온다.
    // 2. login 페이지에 아이디 비밀번호를 입력한다.
    // 3. 성공하면 my-profile로 이동한다.

    // 로그인 페이지를 위한 GetMapping
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 로그인 성공 후 로그인 여부를 위한 GetMapping
    @GetMapping("/my-profile")
    public String myProfile() {
        return "my-profile";
    }

    // 1. 사용자가 register 페이지로 온다.
    // 2. 사용자가 register 페이지에 ID, 비밀번호, 비밀번호 확인을 입력한다.
    // 3. register 페이지에서 /users/register 로 POST 요청
    // 4. UserDetailManager 에 새로운 정보 추가

    @GetMapping("/register")
    public String registerForm() {
        return "register-form";
    }

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserDetailsManager manager,
            PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck
    ) {
        // TODO 사용자가 입력한 아이디 비밀번호 확인 값을 확인해보세요.
        if (password.equals(passwordCheck)) {
            // username 중복도 확인해야 하지만, 이 부분은 service 에서 확인하는 것도 나쁘지 않음
            manager.createUser(User.withUsername(username)
                    .password(password)
                    .build());
        }

        return "redirect:/users/login";
    }


}
