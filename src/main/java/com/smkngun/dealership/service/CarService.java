package com.smkngun.dealership.service;

import com.smkngun.dealership.dto.CarDto;
import com.smkngun.dealership.dto.CarForm;
import com.smkngun.dealership.model.Car;
import com.smkngun.dealership.model.CarStatus;
import com.smkngun.dealership.exception.NotFoundException;
import com.smkngun.dealership.mapper.CarMapper;
import com.smkngun.dealership.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Service marks this as a business-logic bean. Spring will scan and instantiate it.
@Service
// Final fields + @RequiredArgsConstructor = constructor injection, no @Autowired noise.
@RequiredArgsConstructor
// Default to read-only DB transactions (faster); writes override with @Transactional below.
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    // ---------- Read ----------

    public List<CarDto> listAll() {
        return carRepository.findAll().stream().map(carMapper::toDto).toList();
    }

    public List<CarDto> listAvailable() {
        return carRepository.findByStatusOrderByCreatedAtDesc(CarStatus.AVAILABLE)
                .stream().map(carMapper::toDto).toList();
    }

    public List<CarDto> search(String query) {
        // If the user didn't type anything, fall back to "show every available car".
        if (query == null || query.isBlank()) return listAvailable();
        return carRepository.searchAvailable(query.trim())
                .stream().map(carMapper::toDto).toList();
    }

    public CarDto getById(Long id) {
        return carMapper.toDto(getmodel(id));
    }

    // Internal lookup that returns the raw model for other services to use.
    public Car getmodel(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found: " + id));
    }
    
    // @Transactional (no readOnly) turns on a read/write transaction.

    @Transactional
    public CarDto create(CarForm form) {
        Car car = carMapper.toNewModel(form);     // Build a fresh Car from the form.
        Car saved = carRepository.save(car);       // Hibernate INSERTs it.
        return carMapper.toDto(saved);
    }

    @Transactional
    public CarDto update(Long id, CarForm form) {
        Car existing = getmodel(id);              // Load the row (throws if missing).
        carMapper.applyForm(form, existing);       // Overwrite fields in-place.
        /** No explicit save() needed: entities loaded inside a transaction are
        "managed", so Hibernate flushes any changes on commit. We'll call
        save anyway for clarity — it's a no-op when the model is already managed. **/
        return carMapper.toDto(carRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!carRepository.existsById(id)) {
            throw new NotFoundException("Car not found: " + id);
        }
        carRepository.deleteById(id);
    }

    // ---------- Stats ----------

    public long countAvailable() {
        return carRepository.countByStatus(CarStatus.AVAILABLE);
    }

    public long countSold() {
        return carRepository.countByStatus(CarStatus.SOLD);
    }
}
