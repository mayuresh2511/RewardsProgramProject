package com.maymar.rewards.program.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record RewardsResponseDto(String userId,
                                 Map<String, Integer> monthWiseRewards,
                                 Integer totalRewards,
                                 List<Transaction> transactions,
                                 LocalDate rewardsAsOn) {
}
