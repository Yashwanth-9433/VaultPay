package com.yash.vaultpay.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DepositRequest {
    private UUID userId;
    private BigDecimal amount;
}