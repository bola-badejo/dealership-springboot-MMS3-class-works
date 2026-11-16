package com.smkngun.dealership.service;

import com.smkngun.dealership.dto.SaleDto;
import com.smkngun.dealership.model.*;
import com.smkngun.dealership.exception.NotFoundException;
import com.smkngun.dealership.mapper.SaleMapper;
import com.smkngun.dealership.repository.CustomerRepository;
import com.smkngun.dealership.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final CarService carService;
    private final SaleMapper saleMapper;

    // Everything inside this method either succeeds together or rolls back together.
    // That's a transaction: it stops us ending up in a weird state (money taken,
    // but car still "available") if something fails halfway.
    @Transactional
    public SaleDto purchase(Long carId, String buyerUsername) {
        // 1. Load the car. If someone else just bought it, status check below catches it.
        Car car = carService.getmodel(carId);
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalStateException("Car is not available for sale");
        }

        // 2. Find the logged-in customer by their username.
        Customer customer = customerRepository.findByUserUsername(buyerUsername)
                .orElseThrow(() -> new NotFoundException("Customer profile missing"));

        // 3. Build + save the sale record.
        Sale sale = Sale.builder()
                .car(car)
                .customer(customer)
                .salePrice(car.getPrice())      // For a teaching app, use the sticker price.
                .build();
        sale = saleRepository.save(sale);

        // 4. Flip the car to SOLD so it drops off the showroom.
        car.setStatus(CarStatus.SOLD);

        return saleMapper.toDto(sale);
    }

    public List<SaleDto> listAll() {
        return saleRepository.findAllByOrderBySaleDateDesc().stream()
                .map(saleMapper::toDto).toList();
    }

    public List<SaleDto> listForCustomer(String username) {
        return customerRepository.findByUserUsername(username)
                .map(c -> saleRepository.findByCustomerIdOrderBySaleDateDesc(c.getId()))
                .orElse(List.of())
                .stream().map(saleMapper::toDto).toList();
    }

    public BigDecimal totalRevenue() {
        return saleRepository.totalRevenue();
    }

    public long totalSalesCount() {
        return saleRepository.count();
    }
}
