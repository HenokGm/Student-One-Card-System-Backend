package com.socs.oneCardIDManagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IDCardRepository extends MongoRepository<IDCard, String> {
    Optional<IDCard> findByStudentId(String studentId);
}
