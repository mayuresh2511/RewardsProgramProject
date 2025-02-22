package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;

public interface RewardsService {
    RewardsResponseDto calculateLifetimeRewards(String userId);

    RewardsResponseDto calculateRewardsForLastThreeMonths(String userId);

    RewardsResponseDto calculateRewardsForGivenPeriod(String userId, String startDate, String endDate);
}
