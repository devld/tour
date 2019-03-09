package me.devld.tour.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class SecurityUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public SecurityUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!"ld".equals(username) && !"admin".equals(username)) {
            throw new UsernameNotFoundException("username '" + username + "' not found");
        }
        return new User(
                username,
                passwordEncoder.encode(username + "123"),
                Collections.singleton(new SimpleGrantedAuthority("admin".equals(username) ? "ROLE_ADMIN" : "ROLE_USER"))
        );
    }
}
