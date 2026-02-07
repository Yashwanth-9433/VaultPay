package com.yash.vaultpay.service;

import com.yash.vaultpay.dto.DepositRequest;
import com.yash.vaultpay.dto.TransferRequest;
import com.yash.vaultpay.model.Account;
import com.yash.vaultpay.model.Transaction;
import com.yash.vaultpay.model.Transaction.TransactionStatus;
import com.yash.vaultpay.repository.AccountRepository;
import com.yash.vaultpay.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional // Ensures Balance Update + Transaction History happen together
    public Account deposit(DepositRequest request) {
        // 1. Find the Account
        Account account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 2. Add Money
        account.setBalance(account.getBalance().add(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        // 3. Create Transaction Record (Self-Transfer indicates Deposit)
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(account.getId());   // From Self
        transaction.setReceiverAccountId(account.getId()); // To Self
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);

        return updatedAccount;
    }

    @Transactional // CRITICAL: If any part fails (e.g., sender has no money), the whole thing rolls back.
    public void transfer(TransferRequest request) {
        // 1. Retrieve both accounts
        Account sender = accountRepository.findByUserId(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Account receiver = accountRepository.findByUserId(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 2. Check Balance
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds!");
        }

        // 3. Move Money
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // 4. Create Transaction Record
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(sender.getId());
        transaction.setReceiverAccountId(receiver.getId());
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);
    }

    public java.util.List<Transaction> getTransactionHistory(UUID userId) {
        // 1. Find the Account ID for this user
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 2. Fetch all transactions involving this account
        // Note: We use the account ID for both sender and receiver to capture money IN and money OUT
        return transactionRepository.findBySenderAccountIdOrReceiverAccountIdOrderByTimestampDesc(account.getId(), account.getId());
    }
}