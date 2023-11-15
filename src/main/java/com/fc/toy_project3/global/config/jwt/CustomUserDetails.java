package com.fc.toy_project3.global.config.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long memberId;
    private final String nickname;
    private final String name;
    private final String email;
    private final String password;

    public CustomUserDetails(Long memberId, String nickname, String email, String name, String password) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.name = name;
        this.email=email;
        this.password = password;
    }
    public Long getMemberId() {
        return memberId;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() { return password;}

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}