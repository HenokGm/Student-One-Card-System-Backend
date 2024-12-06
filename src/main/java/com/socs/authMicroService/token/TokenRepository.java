package com.socs.authMicroService.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TokenRepository extends MongoRepository<Token, String> {

  List<Token> findByUserIdAndExpiredFalseOrRevokedFalse(Integer userId);

  Optional<Token> findByToken(String token);

  @Query("{ 'user.id': ?0, 'expired': false, 'revoked': false }")
  List<Token> findAllValidTokenByUser(String string);
}
