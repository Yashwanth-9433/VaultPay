package com.yash.vaultpay.repository;

import com.yash.vaultpay.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    // Fetch all transactions where I am the sender OR the receiver, sorted by newest first
    List<Transaction> findBySenderAccountIdOrReceiverAccountIdOrderByTimestampDesc(UUID senderId, UUID receiverId);
}