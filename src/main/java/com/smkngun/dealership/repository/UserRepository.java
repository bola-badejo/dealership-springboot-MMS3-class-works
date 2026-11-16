package com.smkngun.dealership.repository;

import com.smkngun.dealership.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Extending JpaRepository<User, Long> gives us save, findById, findAll, delete, etc. for free.
// The two generic parameters are <model type, ID type>.
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data parses the method NAME at startup and writes the SQL for us.
    // "findByUsername" -> SELECT * FROM users WHERE username = ?
    // Returning Optional<T> forces the caller to handle "not found" safely.
    Optional<User> findByUsername(String username);

    // Used during registration to prevent duplicate emails.
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
