package com.socs.laptopMicroService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/socs/laptop")
@RequiredArgsConstructor
public class LaptopController {

    private final LaptopService service;

    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody LaptopRequest request) {
        service.save(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<Laptop>> findAllBooks() {
        return ResponseEntity.ok(service.findAll());
    }
    // controller that returns a laptop with a given serial number

    @GetMapping("/{serialNumber}")
    public ResponseEntity<Laptop> findLaptopBySerialNumber(@RequestBody String serialNumber) {
        return ResponseEntity.ok(service.findBySerialNumber(serialNumber));
    }
    // find a laptop by student id

    // function to register new laptop
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LaptopRequest request) {
        service.save(request);
        return ResponseEntity.ok().build();
    }
    // function to get all laptops

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllLaptops() {
        return ResponseEntity.ok(service.findAll());
    }

    // function to get laptop by student id first check if laptop exists
    @GetMapping("/get-laptop/{studentId}")
    public ResponseEntity<?> getLaptopByStudentId(@PathVariable String studentId) {
        Laptop laptop = service.findLaptopByStudentId(studentId);
        if (laptop == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(laptop);
    }
}
