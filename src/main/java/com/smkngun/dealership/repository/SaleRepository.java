package com.smkngun.dealership.repository;

import com.smkngun.dealership.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // All sales, newest first — useful for admin dashboard timelines.
    List<Sale> findAllByOrderBySaleDateDesc();

    // Sales for one customer (so they can see their own purchase history).
    List<Sale> findByCustomerIdOrderBySaleDateDesc(Long customerId);

    // SUM is an aggregate: it returns one row of totals, not many.
    // COALESCE(x, 0) = "if x is null, use 0 instead" — happens when there are no sales yet.
    @Query("SELECT COALESCE(SUM(s.salePrice), 0) FROM Sale s")
    BigDecimal totalRevenue();
}
