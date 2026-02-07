package com.yash.vaultpay.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferRequest {
    private UUID senderId;
    private UUID receiverId;
    private BigDecimal amount;
}