package com.smkngun.dealership.mapper;

import com.smkngun.dealership.dto.SaleDto;
import com.smkngun.dealership.model.Sale;
import org.springframework.stereotype.Component;

// Builds the flat SaleDto from the related Sale/Car/Customer graph.
@Component
public class SaleMapper {

    public SaleDto toDto(Sale s) {
        // Compose a human-friendly car summary like "2022 Toyota Camry".
        String carSummary = s.getCar().getYear() + " "
                          + s.getCar().getMake() + " "
                          + s.getCar().getModel();

        // Pull the customer's name from the linked User (fallback to username).
        String customerName = s.getCustomer().getUser().getFullName();
        if (customerName == null || customerName.isBlank()) {
            customerName = s.getCustomer().getUser().getUsername();
        }

        return SaleDto.builder()
                .id(s.getId())
                .carSummary(carSummary)
                .customerName(customerName)
                .salePrice(s.getSalePrice())
                .saleDate(s.getSaleDate())
                .build();
    }
}
