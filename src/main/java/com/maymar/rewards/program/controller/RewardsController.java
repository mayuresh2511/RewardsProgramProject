package com.maymar.rewards.program.controller;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.service.RewardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rewards/api")
public class RewardsController {

    final private RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService){
        this.rewardsService = rewardsService;
    }

    @GetMapping("/lastThreeMonths")
    public RewardsResponseDto getLastThreeMonthRewards(@RequestParam("userId") String userId){
        return rewardsService.calculateRewardsForLastThreeMonths(userId);
    }

    @GetMapping("/specifiedPeriod")
    public RewardsResponseDto getRewardsForGivenPeriod(@RequestParam("userId") String userId,
                                                       @RequestParam("startDate") String startDate,
                                                       @RequestParam("endDate") String endDate){
        return rewardsService.calculateRewardsForGivenPeriod(userId, startDate, endDate);
    }
}
