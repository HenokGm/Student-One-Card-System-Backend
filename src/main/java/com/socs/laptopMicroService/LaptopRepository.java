package com.socs.laptopMicroService;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LaptopRepository extends MongoRepository<Laptop, String> {

    Laptop findBySerialNumber(String serialNumber);

    Laptop findLaptopByStudentId(String studentId);
}