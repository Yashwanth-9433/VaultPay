package com.yash.vaultpay.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID senderAccountId;

    @Column(nullable = false)
    private UUID receiverAccountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }
}