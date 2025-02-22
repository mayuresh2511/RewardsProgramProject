package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import com.maymar.rewards.program.repository.RewardsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService{

    final private RewardsRepository rewardsRepository;

    public RewardsServiceImpl(RewardsRepository rewardsRepository){
        this.rewardsRepository = rewardsRepository;
    }

    @Override
    public RewardsResponseDto calculateLifetimeRewards(String userId) {

        List<CustomerTransactionsEntity> customerTransactions = rewardsRepository.
                findByUserId(userId).
                orElseThrow();

        final Map<String, Integer> resultMap = getResultMap(customerTransactions);

        return new RewardsResponseDto(userId,
                resultMap,
                resultMap.values().stream().mapToInt(Integer::intValue).sum(),
                LocalDate.now()
        );
    }

    @Override
    public RewardsResponseDto calculateRewardsForLastThreeMonths(String userId) {

        return calculateRewardsForGivenPeriod(userId,
                LocalDate.now().minusMonths(3).toString(),
                LocalDate.now().toString()
        );
    }

    @Override
    public RewardsResponseDto calculateRewardsForGivenPeriod(String userId, String startDateString, String endDateString) {
        //String startDateString = "2025-01-01";
        LocalDate startDate = LocalDate.parse(startDateString);

        //String endDateString = "2025-02-28";
        LocalDate endDate = LocalDate.parse(endDateString);

        List<CustomerTransactionsEntity> customerTransactions = rewardsRepository.
                findByUserIdAndDateRange(userId, startDate, endDate).
                orElseThrow();

        final Map<String, Integer> resultMap = getResultMap(customerTransactions);

        return new RewardsResponseDto(userId,
                resultMap,
                resultMap.values().stream().mapToInt(Integer::intValue).sum(),
                LocalDate.now()
        );
    }

    private static Map<String, Integer> getResultMap(List<CustomerTransactionsEntity> customerTransactions) {
        //To Group the Transactions By Month-Year pair
        Map<String, List<CustomerTransactionsEntity>> groupedByMonth = customerTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTranDate().getMonth().toString() + "-" + transaction.getTranDate().getYear()
                ));

        //To Get The total for each Month-Year pair
        return groupedByMonth.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        (entry) -> {
                            return entry.getValue()
                                    .stream()
                                    .filter(transaction -> transaction.getTranAmt() > 50)
                                    .map((transaction) -> {
                                        if (transaction.getTranAmt() < 100) {
                                            return transaction.getTranAmt() - 50;
                                        } else {
                                            return ((transaction.getTranAmt() - 100) * 2) + 50;
                                        }
                                    }).mapToInt(Integer::intValue).sum();
                        })
                );
    }
}
