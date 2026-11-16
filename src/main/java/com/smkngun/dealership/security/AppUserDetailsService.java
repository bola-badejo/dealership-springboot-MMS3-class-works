package com.smkngun.dealership.security;

import com.smkngun.dealership.model.User;
import com.smkngun.dealership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

// Spring Security calls loadUserByUsername() during login. Our job is to look
// up the user and return a UserDetails object describing their idmodel + roles.
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user or throw — Spring Security turns this into a 401 for the login page.
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Spring Security expects roles prefixed with "ROLE_". hasRole("ADMIN") checks "ROLE_ADMIN".
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));

        // Build the framework's immutable user wrapper.
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())      // Already BCrypt-hashed in the DB.
                .disabled(!u.isEnabled())
                .authorities(authorities)
                .build();
    }
}
