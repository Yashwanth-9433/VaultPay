package com.yash.vaultpay.repository;

import com.yash.vaultpay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Spring generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring generates: SELECT count(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);
}