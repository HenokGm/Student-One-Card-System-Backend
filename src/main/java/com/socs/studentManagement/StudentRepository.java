package com.socs.studentManagement;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {

  Optional<Student> findByEmail(String email);

  @SuppressWarnings("null")
  Optional<Student> findById(String studentId);

}
