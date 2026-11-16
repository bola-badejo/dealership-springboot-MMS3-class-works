package com.smkngun.dealership.service;

import com.smkngun.dealership.dto.RegistrationForm;
import com.smkngun.dealership.model.Customer;
import com.smkngun.dealership.model.Role;
import com.smkngun.dealership.model.User;
import com.smkngun.dealership.repository.CustomerRepository;
import com.smkngun.dealership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Handles sign-up and user bookkeeping.
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;   // Supplied by SecurityConfig (BCrypt).

    @Transactional
    public User registerCustomer(RegistrationForm form) {
        // Fail-fast checks so we return a clear error instead of a DB constraint crash.
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Build the User with a securely hashed password.
        User user = User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))   // BCrypt hash.
                .email(form.getEmail())
                .fullName(form.getFullName())
                .role(Role.CUSTOMER)                                    // Self-service = customer.
                .enabled(true)
                .build();
        user = userRepository.save(user);

        // Create the matching Customer profile row.
        Customer customer = Customer.builder()
                .user(user)
                .phone(form.getPhone())
                .address(form.getAddress())
                .city(form.getCity())
                .build();
        customerRepository.save(customer);

        return user;
    }
}
