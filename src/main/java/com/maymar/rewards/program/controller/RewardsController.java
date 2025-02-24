package com.maymar.rewards.program.controller;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.exception.custom.InvalidRequestParameterException;
import com.maymar.rewards.program.helper.RewardsPeriod;
import com.maymar.rewards.program.service.RewardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/rewards/api")
public class RewardsController {

    final private RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService){
        this.rewardsService = rewardsService;
    }

    @GetMapping("/calculateRewards")
    public RewardsResponseDto getRewardsForGivenPeriod(@RequestParam("userId") String userId,
                                                       @RequestParam("period") RewardsPeriod rewardsPeriod,
                                                       @RequestParam(value = "startDate", required = false) String startDateString,
                                                       @RequestParam(value = "endDate", required = false) String endDateString){

        if (userId.trim().isEmpty() || userId.trim().length() > 20)
            throw new InvalidRequestParameterException("Please enter a valid userId. Non empty and less than 20 chars..");

        if (rewardsPeriod.equals(RewardsPeriod.LIFETIME)){
            return rewardsService.calculateLifetimeRewards(userId);
        } else if (rewardsPeriod.equals(RewardsPeriod.LASTTHREEMONTHS)) {
            return rewardsService.calculateRewardsForLastThreeMonths(userId);
        } else {
            if (startDateString.trim().isEmpty() || endDateString.trim().isEmpty())
                throw new InvalidRequestParameterException("Please enter a valid date. Non empty and in 'YYYY-MM-DD' format");
            return rewardsService.calculateRewardsForGivenPeriod(userId,
                    LocalDate.parse(startDateString),
                    LocalDate.parse(endDateString));
        }
    }
}
