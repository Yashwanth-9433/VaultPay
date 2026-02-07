package com.yash.vaultpay.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Version // Optimistic Locking (Prevents double spending)
    private Long version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Constructor to init empty account
    public Account(User user) {
        this.user = user;
        this.balance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }
}