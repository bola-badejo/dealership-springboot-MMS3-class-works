package com.smkngun.dealership.model;

import jakarta.persistence.*;
import lombok.*;

// A Customer is the "profile" side of a shopper: phone, address, etc.
// We split it from User so that User stays focused on auth (login/password),
// and Customer holds everything you'd actually need to complete a sale.
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @OneToOne: every Customer has exactly ONE User account, and vice versa.
    // @JoinColumn names the foreign-key column in the "customers" table.
    // optional=false: a Customer MUST point at a User (no orphans).
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 30)
    private String phone;

    @Column(length = 200)
    private String address;

    @Column(length = 80)
    private String city;
}
