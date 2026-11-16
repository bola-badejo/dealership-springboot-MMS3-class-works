package com.smkngun.dealership.model;

// A plain enum lists the only two kinds of users this app understands.
// Storing roles as an enum (instead of a free-form String) keeps our data clean
// and lets the compiler catch typos like "ADMINN".
public enum Role {
    ADMIN,      // Full access to the admin dashboard and all CRUD operations.
    CUSTOMER    // Regular shopper — can browse cars and buy them.
}
