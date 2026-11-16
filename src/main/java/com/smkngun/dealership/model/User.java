package com.smkngun.dealership.model;

// --- JPA (Jakarta Persistence) imports let us map this Java class to a DB table.
import jakarta.persistence.*;
// --- Lombok removes boilerplate (getters/setters/constructors) at compile time.
import lombok.*;

import java.time.LocalDateTime;

// @Entity: tells Hibernate "this class is a database table".
@Entity
// @Table: lets us rename the table. "users" (plural) avoids clashing with the
// reserved word "user" in Postgres.
@Table(name = "users")
// --- Lombok annotations ---
@Getter                        // Generates getX() for every field.
@Setter                        // Generates setX() for every field.
@NoArgsConstructor             // JPA REQUIRES a no-arg constructor to build entities.
@AllArgsConstructor            // Convenience: build a User with every field at once.
@Builder                       // Lets us write User.builder().username("x")...build()
public class User {

    // @Id marks this field as the primary key.
    @Id
    // @GeneratedValue asks the DB to auto-assign the number (1, 2, 3...).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column fine-tunes the DB column. unique=true adds a UNIQUE constraint.
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // We NEVER store plain passwords. This holds the BCrypt-hashed password.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "full_name", length = 120)
    private String fullName;

    // @Enumerated(STRING) stores the enum value as text ("ADMIN") not a number.
    // STRING is safer: if we add a new role later, the mapping doesn't shift.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    // Flag we can flip to disable a login without deleting the row.
    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @PrePersist runs once, just before the row is first INSERTed.
    // Perfect place to stamp a creation timestamp.
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
