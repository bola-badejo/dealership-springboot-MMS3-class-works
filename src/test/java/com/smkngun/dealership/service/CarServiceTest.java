package com.smkngun.dealership.service;

import com.smkngun.dealership.dto.CarForm;
import com.smkngun.dealership.model.Car;
import com.smkngun.dealership.exception.NotFoundException;
import com.smkngun.dealership.mapper.CarMapper;
import com.smkngun.dealership.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pure unit test: no Spring context, no DB. We mock the repository and verify
// that the service applies the right rules on top of it.
@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock CarRepository carRepository;
    @Mock CarMapper carMapper;                  // We don't care about mapping here.

    @InjectMocks CarService carService;

    @Test
    void create_savesNewCar() {
        // --- Arrange: build a form that would come from the HTML page.
        CarForm form = new CarForm();
        form.setMake("Toyota");
        form.setModel("Corolla");
        form.setYear(2022);
        form.setPrice(new BigDecimal("10000000"));

        Car model = Car.builder().make("Toyota").model("Corolla").build();
        when(carMapper.toNewModel(form)).thenReturn(model);
        when(carRepository.save(model)).thenReturn(model);

        // --- Act
        carService.create(form);

        // --- Assert: the repository really was called with our mapped model.
        verify(carRepository).save(model);
    }

    @Test
    void getById_missing_throwsNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // assertThatThrownBy reads like English — one of AssertJ's best features.
        assertThatThrownBy(() -> carService.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Car not found");
    }
}
