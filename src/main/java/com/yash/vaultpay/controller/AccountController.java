package com.yash.vaultpay.controller;

import com.yash.vaultpay.dto.DepositRequest;
import com.yash.vaultpay.model.Account;
import com.yash.vaultpay.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        try {
            Account account = accountService.deposit(request);
            return ResponseEntity.ok("Deposit successful! New Balance: " + account.getBalance());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody com.yash.vaultpay.dto.TransferRequest request) {
        try {
            accountService.transfer(request);
            return ResponseEntity.ok("Transfer successful!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<?> getHistory(@PathVariable java.util.UUID userId) {
        return ResponseEntity.ok(accountService.getTransactionHistory(userId));
    }
}