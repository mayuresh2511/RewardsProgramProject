package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import com.maymar.rewards.program.exception.custom.InvalidUserIdException;
import com.maymar.rewards.program.exception.custom.NoTransactionsFoundException;
import com.maymar.rewards.program.repository.RewardsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
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
                new CustomerTransactionsEntity(1, "Mayuresh", 200, LocalDate.parse("2025-01-15")),
                new CustomerTransactionsEntity(2, "Mayuresh", 40, LocalDate.parse("2025-01-20")),
                new CustomerTransactionsEntity(3, "Mayuresh", 150, LocalDate.parse("2025-02-15")),
                new CustomerTransactionsEntity(4, "Mayuresh", 350, LocalDate.parse("2025-02-20"))
        );

        when(rewardsRepository.findByUserId(Mockito.anyString())).
                thenReturn(Optional.of(transactions));

        RewardsResponseDto result = rewardsService.calculateLifetimeRewards("Mayuresh");

        Assertions.assertNotNull(result);
    }

    @Test
    public void calculateLifetimeRewardsTest_Should_ThrowInvalidUserIdException(){

        Assertions.assertThrows(InvalidUserIdException.class,
                () -> {
                    rewardsService.calculateLifetimeRewards("    ");
                },
                "Invalid userId format..."
        );
    }

    @Test
    public void calculateRewardsForGivenPeriodTest_Should_ReturnsRewardsResponseDTO(){
        List<CustomerTransactionsEntity> transactions = List.of(
                new CustomerTransactionsEntity(1, "Mayuresh", 200, LocalDate.parse("2025-01-15")),
                new CustomerTransactionsEntity(2, "Mayuresh", 40, LocalDate.parse("2025-01-20")),
                new CustomerTransactionsEntity(3, "Mayuresh", 150, LocalDate.parse("2025-02-15")),
                new CustomerTransactionsEntity(4, "Mayuresh", 350, LocalDate.parse("2025-02-20"))
        );

        when(rewardsRepository.findByUserIdAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).
                thenReturn(Optional.of(transactions));

        RewardsResponseDto result = rewardsService.calculateRewardsForGivenPeriod("Mayuresh",
                "2025-01-01",
                "2025-02-28");

        Assertions.assertNotNull(result);
    }

    @Test
    public void calculateRewardsForGivenPeriodTest_Should_ThrowsDateTimeException(){

        Assertions.assertThrows(DateTimeException.class,
                () -> {
                    rewardsService.calculateRewardsForGivenPeriod("Mayuresh",
                    "2025-01-01",
                    "2025-02-30");
                },
                "Entered date is not valid..."
        );
    }

    @Test
    public void calculateRewardsForGivenPeriodTest_Should_ThrowNoSuchElementException(){

        when(rewardsRepository.findByUserIdAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).
                thenReturn(Optional.empty());


        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    rewardsService.calculateRewardsForGivenPeriod("Mayuresh",
                            "2025-01-01",
                            "2025-02-28");
                },
                "No records for given user and period..."
        );
    }
}
