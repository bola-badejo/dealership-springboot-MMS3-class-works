package com.smkngun.dealership.config;

import com.smkngun.dealership.model.*;
import com.smkngun.dealership.repository.CarRepository;
import com.smkngun.dealership.repository.CustomerRepository;
import com.smkngun.dealership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

// A CommandLineRunner's run() method fires ONCE after the Spring context is up.
// We use it to seed a demo admin + a few cars so the app looks alive on first boot.
@Component
// @Profile("!test") = "load this bean UNLESS the 'test' profile is active".
// Keeps our tests clean of seed data.
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Guard: only seed when the DB is empty, so we don't duplicate on each restart.
        if (userRepository.count() > 0) {
            log.info("Seed skipped — users already present.");
            return;
        }

        log.info("Seeding demo data...");

        // ---- Default admin account ----
        User admin = userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@smkngun.test")
                .fullName("Shop Admin")
                .role(Role.ADMIN)
                .enabled(true)
                .build());

        // ---- Default customer account ----
        User customerUser = userRepository.save(User.builder()
                .username("jane")
                .password(passwordEncoder.encode("jane123"))
                .email("jane@example.com")
                .fullName("Jane Doe")
                .role(Role.CUSTOMER)
                .enabled(true)
                .build());
        customerRepository.save(Customer.builder()
                .user(customerUser)
                .phone("0803 000 0000")
                .address("12 Allen Avenue")
                .city("Lagos")
                .build());

        // ---- Starter inventory ----
        carRepository.saveAll(List.of(
                Car.builder()
                        .make("Toyota").model("Camry").year(2022)
                        .price(new BigDecimal("18500000"))
                        .mileage(42000).color("Pearl White").bodyType("Sedan")
                        .transmission("Automatic").fuelType("Petrol")
                        .imageUrl("https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800")
                        .description("Clean title, single-owner. Full service history.")
                        .status(CarStatus.AVAILABLE).build(),

                Car.builder()
                        .make("Honda").model("CR-V").year(2021)
                        .price(new BigDecimal("22000000"))
                        .mileage(58000).color("Midnight Blue").bodyType("SUV")
                        .transmission("Automatic").fuelType("Petrol")
                        .imageUrl("https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800")
                        .description("Family SUV with all-wheel drive and roof rails.")
                        .status(CarStatus.AVAILABLE).build(),

                Car.builder()
                        .make("Lexus").model("RX 350").year(2023)
                        .price(new BigDecimal("45000000"))
                        .mileage(15000).color("Obsidian Black").bodyType("SUV")
                        .transmission("Automatic").fuelType("Hybrid")
                        .imageUrl("https://images.unsplash.com/photo-1544636331-e26879cd4d9b?w=800")
                        .description("Flagship comfort. Mark Levinson audio and panoramic roof.")
                        .status(CarStatus.AVAILABLE).build(),

                Car.builder()
                        .make("Mercedes-Benz").model("C300").year(2020)
                        .price(new BigDecimal("28000000"))
                        .mileage(65000).color("Silver").bodyType("Sedan")
                        .transmission("Automatic").fuelType("Petrol")
                        .imageUrl("https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800")
                        .description("Executive sedan with AMG line trim.")
                        .status(CarStatus.AVAILABLE).build(),

                Car.builder()
                        .make("Ford").model("Ranger").year(2022)
                        .price(new BigDecimal("31000000"))
                        .mileage(30000).color("Race Red").bodyType("Pickup")
                        .transmission("Manual").fuelType("Diesel")
                        .imageUrl("https://images.unsplash.com/photo-1591293836027-e05b48473b67?w=800")
                        .description("4x4 workhorse ready for any terrain.")
                        .status(CarStatus.AVAILABLE).build(),

                Car.builder()
                        .make("Tesla").model("Model 3").year(2023)
                        .price(new BigDecimal("42000000"))
                        .mileage(8000).color("Deep Blue").bodyType("Sedan")
                        .transmission("Automatic").fuelType("Electric")
                        .imageUrl("https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=800")
                        .description("Long-range EV. Full self-driving hardware.")
                        .status(CarStatus.AVAILABLE).build()
        ));

        log.info("Seeded {} users and {} cars", userRepository.count(), carRepository.count());
        log.info("Login as admin/admin123 (admin) or jane/jane123 (customer).");
    }
}
