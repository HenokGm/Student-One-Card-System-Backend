package com.socs.workersManagement;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);

  // Count users based on their role
  long countByRole(Role role);
}
