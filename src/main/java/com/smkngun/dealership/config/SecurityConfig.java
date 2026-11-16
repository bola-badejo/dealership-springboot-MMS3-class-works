package com.smkngun.dealership.config;

import com.smkngun.dealership.security.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration = "this class defines Spring beans".
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService userDetailsService;

    // BCrypt is the industry-standard password hash — slow on purpose, salted per user.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Wires our UserDetailsService + password encoder into Spring's auth pipeline.
    // In Spring Security 7 the UserDetailsService is passed through the constructor.
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    // THE main security rules — read top to bottom, first match wins.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())

            // ---- Authorization rules per URL pattern ----
            .authorizeHttpRequests(auth -> auth
                // Public pages: anyone (logged in or not) can see these.
                .requestMatchers(
                        "/", "/cars", "/cars/*",
                        "/login", "/register",
                        "/css/**", "/js/**", "/images/**",
                        "/h2-console/**"            // Allow the dev DB console.
                ).permitAll()

                // Anything under /admin/** requires the ADMIN role.
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Customer area requires CUSTOMER (or ADMIN).
                .requestMatchers("/account/**", "/buy/**").hasAnyRole("CUSTOMER", "ADMIN")

                // Everything else requires some kind of login.
                .anyRequest().authenticated()
            )

            // ---- Form-based login page ----
            .formLogin(form -> form
                .loginPage("/login")                // Our custom Thymeleaf login page.
                .defaultSuccessUrl("/post-login", true)  // Router decides where to send them.
                .failureUrl("/login?error")
                .permitAll()
            )

            // ---- Logout ----
            .logout(logout -> logout
                .logoutUrl("/logout")                // Spring Security handles POST /logout.
                .logoutSuccessUrl("/?logout")
                .permitAll()
            )

            // ---- CSRF + H2 console tweaks (teaching note: NEVER disable CSRF in production
            // except for stateless APIs. H2 console uses frames and ignores CSRF, so we
            // exempt it for development only.) ----
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(h -> h.frameOptions(f -> f.sameOrigin()))

            // Stateful (session-backed) auth is the default for form login.
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}
