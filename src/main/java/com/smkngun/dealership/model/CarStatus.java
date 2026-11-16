package com.smkngun.dealership.model;

// Lifecycle of a car in our inventory.
// Using an enum here means a car can ONLY be in one of these three states.
public enum CarStatus {
    AVAILABLE,  // On sale: shows up on the public showroom.
    RESERVED,   // A customer has placed a hold (future enhancement).
    SOLD        // Transaction complete: hidden from the showroom.
}
