package com.team1.spreet.security;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private final User user;

    public User getUser() {
        return this.user;
    }

//    private final String loginId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = user.getUserRole();
        String authority = userRole.getRole();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.getUser().getLoginId();
    }

    @Override
    public String getPassword() {
        return this.getUser().getPassword();
    }

    //false로 바꿀것
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //false로 바꿀것
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //false로 바꿀것
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //false로 바꿀것
    @Override
    public boolean isEnabled() {
        return true;
    }
}
