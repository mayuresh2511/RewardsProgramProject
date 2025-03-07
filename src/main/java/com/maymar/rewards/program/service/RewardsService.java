package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;

import java.time.LocalDate;

public interface RewardsService {
    RewardsResponseDto calculateLifetimeRewards(String userId);

    RewardsResponseDto calculateRewardsForLastThreeMonths(String userId);

    RewardsResponseDto calculateRewardsForGivenPeriod(String userId, LocalDate startDate, LocalDate endDate);
}
