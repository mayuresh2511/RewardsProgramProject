package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import com.maymar.rewards.program.exception.custom.NoTransactionsFoundException;
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

        if (customerTransactions.isEmpty())
            throw new NoTransactionsFoundException("There are no transactions present for userId " + userId);

        return processAndPrepareResponse(customerTransactions, userId);
    }

    @Override
    public RewardsResponseDto calculateRewardsForLastThreeMonths(String userId) {

        return calculateRewardsForGivenPeriod(userId,
                LocalDate.now().minusMonths(3),
                LocalDate.now()
        );
    }

    @Override
    public RewardsResponseDto calculateRewardsForGivenPeriod(String userId, LocalDate startDate, LocalDate endDate) {
        List<CustomerTransactionsEntity> customerTransactions = rewardsRepository.
                findByUserIdAndDateRange(userId, startDate, endDate).
                orElseThrow();

        if (customerTransactions.isEmpty())
            throw new NoTransactionsFoundException("There are no transactions present for userId " + userId);

        return processAndPrepareResponse(customerTransactions, userId);
    }

    private static RewardsResponseDto processAndPrepareResponse(List<CustomerTransactionsEntity> customerTransactions, String userId) {
        //To Group the Transactions By Month-Year pair
        Map<String, List<CustomerTransactionsEntity>> groupedByMonth = customerTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTranDate().getMonth().toString() +
                                "-" +
                                transaction.getTranDate().getYear()
                ));

        //To Get The total for each Month-Year pair
        Map<String, Integer> resultMap = groupedByMonth.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        (entry) -> {
                            return entry.getValue()
                                    .stream()
                                    .filter(transaction -> transaction.getTranAmt() > 50)
                                    .map(RewardsServiceImpl::rewardsCalculator).mapToInt(Integer::intValue).sum();
                        })
                );

        return new RewardsResponseDto(userId,
                resultMap,
                resultMap.values().stream().mapToInt(Integer::intValue).sum(),
                LocalDate.now()
        );
    }

    private static int rewardsCalculator(CustomerTransactionsEntity transaction) {
        if (transaction.getTranAmt() < 100) {
            return transaction.getTranAmt() - 50;
        } else {
            return ((transaction.getTranAmt() - 100) * 2) + 50;
        }
    }
}
