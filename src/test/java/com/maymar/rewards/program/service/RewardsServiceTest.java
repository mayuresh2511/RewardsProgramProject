package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import com.maymar.rewards.program.repository.RewardsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    @Mock
    private RewardsRepository rewardsRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @Test
    public void calculateLifetimeRewardsTest_Should_ReturnsRewardsResponseDTO(){
        List<CustomerTransactionsEntity> transactions = List.of(
                new CustomerTransactionsEntity(1, "Mayuresh", new BigDecimal(200), LocalDate.parse("2025-01-15")),
                new CustomerTransactionsEntity(2, "Mayuresh", new BigDecimal(40), LocalDate.parse("2025-01-20")),
                new CustomerTransactionsEntity(3, "Mayuresh", new BigDecimal(150), LocalDate.parse("2025-02-15")),
                new CustomerTransactionsEntity(4, "Mayuresh", new BigDecimal(350), LocalDate.parse("2025-02-20"))
        );

        when(rewardsRepository.findByUserId(Mockito.anyString())).
                thenReturn(Optional.of(transactions));

        RewardsResponseDto result = rewardsService.calculateLifetimeRewards("Mayuresh");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.monthWiseRewards().size());
        Assertions.assertEquals(950, result.totalRewards());
        Assertions.assertEquals(3, result.transactions().size());
    }

    @Test
    public void calculateRewardsForGivenPeriodTest_Should_ReturnsRewardsResponseDTO(){
        List<CustomerTransactionsEntity> transactions = List.of(
                new CustomerTransactionsEntity(1, "Mayuresh", new BigDecimal(200), LocalDate.parse("2025-01-15")),
                new CustomerTransactionsEntity(2, "Mayuresh", new BigDecimal(40), LocalDate.parse("2025-01-20")),
                new CustomerTransactionsEntity(3, "Mayuresh", new BigDecimal(150), LocalDate.parse("2025-02-15"))
        );

        when(rewardsRepository.findByUserIdAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).
                thenReturn(Optional.of(transactions));

        RewardsResponseDto result = rewardsService.calculateRewardsForGivenPeriod("Mayuresh",
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-02-15"));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.monthWiseRewards().size());
        Assertions.assertEquals(400, result.totalRewards());
        Assertions.assertEquals(2, result.transactions().size());
    }

    @Test
    public void calculateRewardsForGivenPeriodTest_Should_ThrowNoSuchElementException(){

        when(rewardsRepository.findByUserIdAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).
                thenReturn(Optional.empty());


        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    rewardsService.calculateRewardsForGivenPeriod("Mayuresh",
                            LocalDate.parse("2025-01-01"),
                            LocalDate.parse("2025-02-28"));
                },
                "No records for given user and period..."
        );
    }

    @Test
    public void rewardsCalculatorTest_Should_ReturnRewardsPoints250(){
        CustomerTransactionsEntity customerTransaction = new CustomerTransactionsEntity(1,
                "Mayuresh",
                new BigDecimal(200),
                LocalDate.parse("2025-01-15"));

        int rewardPoints = rewardsService.rewardsCalculator(customerTransaction);

        Assertions.assertEquals(250, rewardPoints);
    }

    @Test
    public void rewardsCalculatorTest_Should_ReturnRewardsPoints0(){
        CustomerTransactionsEntity customerTransaction = new CustomerTransactionsEntity(1,
                "Mayuresh",
                new BigDecimal(50),
                LocalDate.parse("2025-01-15"));

        int rewardPoints = rewardsService.rewardsCalculator(customerTransaction);

        Assertions.assertEquals(0, rewardPoints);
    }
}
