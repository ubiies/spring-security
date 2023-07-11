package com.example.auth.Entity;

// 사용자 정보에 추가적인 정보를 포함하고 싶다면...
// UserDetails 인터이스를 구현하는 클래스

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;

    public static CustomUserDetails fromEntity(
            UserEntity entity
    ) {
//        CustomUserDetails details = new CustomUserDetails();
//        details.id = entity.getId();
//        details.password = entity.getPassword();
//        details.email = entity.getEmail();
//        details.phone = entity.getPhone();
//        return details;

        // 빌더 적용시
        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }

    public UserEntity newEntity() {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setEmail(email);
        entity.setPhone(phone);
        return entity;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    // 권한 설정을 위한 메서드 ex) 사용자, 관리자
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    // --- 유효한 사용자인지 판단하기 위한 메서드 ---
    // 사용자 계정이 만료되었는지 여부
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 사용자의 자격 증명(비밀번호)이 만료되었는지 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 사용자의 자격 증명(비밀번호)이 만료되었는지 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 사용자의 계정이 활성화되었는지 여부
    @Override
    public boolean isEnabled() {
        return false;
    }
}
