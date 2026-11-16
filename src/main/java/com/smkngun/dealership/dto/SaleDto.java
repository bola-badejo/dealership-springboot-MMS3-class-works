package com.smkngun.dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A flat, display-ready view of a Sale. Combines data from Sale + Car + Customer.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long id;
    private String carSummary;      // "2022 Toyota Camry" — handy for tables.
    private String customerName;
    private BigDecimal salePrice;
    private LocalDateTime saleDate;
}
