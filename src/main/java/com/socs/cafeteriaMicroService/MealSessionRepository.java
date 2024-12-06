package com.socs.cafeteriaMicroService;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MealSessionRepository extends MongoRepository<MealSession, String> {

    Optional<MealSession> findByIsActive(boolean isActive); // Find active session

    boolean existsByIsActive(boolean isActive); // Check if any session is active
}
