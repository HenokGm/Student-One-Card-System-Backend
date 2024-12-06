package com.socs.laptopMicroService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaptopService {

    private final LaptopRepository repository;

    public void save(LaptopRequest request) {
        var laptop = Laptop.builder()
                .serialNumber(request.getSerialNumber())
                .studentId(request.getStudentId())
                .brand(request.getBrand())
                .color(request.getColor())
                .model(request.getModel())
                .picture(request.getPicture())
                .build();
        repository.save(laptop);
    }

    public List<Laptop> findAll() {
        return repository.findAll();
    }

    // function to find laptop by serial number
    public Laptop findBySerialNumber(String serialNumber) {
        return repository.findBySerialNumber(serialNumber);
    }

    public Laptop findLaptopByStudentId(String studentId) {
        return repository.findLaptopByStudentId(studentId);
    }

    // function to add new laptop
    public void addLaptop(LaptopRequest request) {
        var laptop = Laptop.builder()
                .serialNumber(request.getSerialNumber())
                .studentId(request.getStudentId())
                .brand(request.getBrand())
                .color(request.getColor())
                .model(request.getModel())
                .picture(request.getPicture())
                .build();
        repository.save(laptop);
    }

}
