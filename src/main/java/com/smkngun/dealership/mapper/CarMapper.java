package com.smkngun.dealership.mapper;

import com.smkngun.dealership.dto.CarDto;
import com.smkngun.dealership.dto.CarForm;
import com.smkngun.dealership.model.Car;
import org.springframework.stereotype.Component;

// A Mapper is a plain class whose job is to copy fields between layers.
// Doing this by hand (no MapStruct) keeps the magic low so students can see every step.
@Component                      // Register as a Spring bean so services can @Autowire it.
public class CarMapper {

    // Model -> DTO (used when rendering pages or returning JSON).
    public CarDto toDto(Car c) {
        if (c == null) return null;
        return CarDto.builder()
                .id(c.getId())
                .make(c.getMake())
                .model(c.getModel())
                .year(c.getYear())
                .price(c.getPrice())
                .mileage(c.getMileage())
                .color(c.getColor())
                .bodyType(c.getBodyType())
                .transmission(c.getTransmission())
                .fuelType(c.getFuelType())
                .imageUrl(c.getImageUrl())
                .description(c.getDescription())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .build();
    }

    // Form -> new Model (used when CREATING a car).
    public Car toNewModel(CarForm f) {
        return Car.builder()
                .make(f.getMake())
                .model(f.getModel())
                .year(f.getYear())
                .price(f.getPrice())
                .mileage(f.getMileage())
                .color(f.getColor())
                .bodyType(f.getBodyType())
                .transmission(f.getTransmission())
                .fuelType(f.getFuelType())
                .imageUrl(f.getImageUrl())
                .description(f.getDescription())
                .build();
    }

    // Model -> Form (used to pre-fill the EDIT page).
    public CarForm toForm(Car c) {
        CarForm f = new CarForm();
        f.setId(c.getId());
        f.setMake(c.getMake());
        f.setModel(c.getModel());
        f.setYear(c.getYear());
        f.setPrice(c.getPrice());
        f.setMileage(c.getMileage());
        f.setColor(c.getColor());
        f.setBodyType(c.getBodyType());
        f.setTransmission(c.getTransmission());
        f.setFuelType(c.getFuelType());
        f.setImageUrl(c.getImageUrl());
        f.setDescription(c.getDescription());
        return f;
    }

    // Copy form fields ONTO an existing entity (used for UPDATE). The entity
    // is already managed by Hibernate, so changes will be persisted on commit.
    public void applyForm(CarForm f, Car c) {
        c.setMake(f.getMake());
        c.setModel(f.getModel());
        c.setYear(f.getYear());
        c.setPrice(f.getPrice());
        c.setMileage(f.getMileage());
        c.setColor(f.getColor());
        c.setBodyType(f.getBodyType());
        c.setTransmission(f.getTransmission());
        c.setFuelType(f.getFuelType());
        c.setImageUrl(f.getImageUrl());
        c.setDescription(f.getDescription());
    }
}
