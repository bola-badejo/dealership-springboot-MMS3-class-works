package com.smkngun.dealership.repository;

import com.smkngun.dealership.model.Car;
import com.smkngun.dealership.model.CarStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest spins up only the JPA layer against an in-memory DB.
// Fast, focused slice test — verifies our repository query actually works.
@DataJpaTest
// Replace.NONE tells Spring "use the DataSource I configured in application.yaml".
// Without this, @DataJpaTest would swap in its own embedded DB and ignore our settings.
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class CarRepositoryTest {

    @Autowired CarRepository carRepository;

    @Test
    void searchAvailable_matchesMakeCaseInsensitively() {
        // --- Arrange: two AVAILABLE cars, one SOLD.
        carRepository.save(Car.builder().make("Toyota").model("Camry").year(2022)
                .price(new BigDecimal("1000")).status(CarStatus.AVAILABLE).build());
        carRepository.save(Car.builder().make("Honda").model("Civic").year(2021)
                .price(new BigDecimal("1000")).status(CarStatus.AVAILABLE).build());
        carRepository.save(Car.builder().make("Toyota").model("Camry").year(2019)
                .price(new BigDecimal("1000")).status(CarStatus.SOLD).build());

        // --- Act: search "toyot" (lowercase, partial).
        var hits = carRepository.searchAvailable("toyot");

        // --- Assert: finds the AVAILABLE Toyota but NOT the SOLD one.
        assertThat(hits).hasSize(1);
        assertThat(hits.get(0).getMake()).isEqualTo("Toyota");
        assertThat(hits.get(0).getStatus()).isEqualTo(CarStatus.AVAILABLE);
    }
}
