package com.yash.vaultpay.repository;

import com.yash.vaultpay.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    // Find the account belonging to a specific user
    Optional<Account> findByUserId(UUID userId);
}