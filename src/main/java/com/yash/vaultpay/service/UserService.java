package com.yash.vaultpay.service;

import com.yash.vaultpay.dto.RegisterRequest;
import com.yash.vaultpay.model.Account;
import com.yash.vaultpay.model.User;
import com.yash.vaultpay.repository.AccountRepository;
import com.yash.vaultpay.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection (Best Practice)
    public UserService(UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // Database Transaction: If Account creation fails, User creation is rolled back.
    public User registerUser(RegisterRequest request) {
        // 1. Validation
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken!");
        }

        // 2. Create User (Identity)
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password!

        // 3. Save User
        User savedUser = userRepository.save(user);

        // 4. Create Account (Wallet) linked to User
        Account account = new Account(savedUser);
        accountRepository.save(account);

        return savedUser;
    }
}