package com.smkngun.dealership.repository;

import com.smkngun.dealership.model.Car;
import com.smkngun.dealership.model.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    // Simple derived query: list every car in a given status, newest first.
    List<Car> findByStatusOrderByCreatedAtDesc(CarStatus status);

    // A custom JPQL query — written when derived names get unwieldy.
    // "LIKE LOWER(CONCAT('%', :q, '%'))" = case-insensitive 'contains' search.
    @Query("""
            SELECT c FROM Car c
             WHERE c.status = com.smkngun.dealership.model.CarStatus.AVAILABLE
               AND (LOWER(c.make)  LIKE LOWER(CONCAT('%', :q, '%'))
                 OR LOWER(c.model) LIKE LOWER(CONCAT('%', :q, '%')))
             ORDER BY c.createdAt DESC
            """)
    List<Car> searchAvailable(String q);

    // Derived count — Spring Data writes SELECT COUNT(*) FROM cars WHERE status=?
    long countByStatus(CarStatus status);
}
