package com.maymar.rewards.program.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(long transactionId,
                          BigDecimal tranAmt,
                          LocalDate tranDate) {
}
