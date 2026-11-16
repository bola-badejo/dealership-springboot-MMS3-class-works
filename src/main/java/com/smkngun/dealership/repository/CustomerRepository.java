package com.smkngun.dealership.repository;

import com.smkngun.dealership.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Nested property navigation: "User.username" -> user.username.
    // Resulting SQL joins customers with users on user_id.
    Optional<Customer> findByUserUsername(String username);
}
