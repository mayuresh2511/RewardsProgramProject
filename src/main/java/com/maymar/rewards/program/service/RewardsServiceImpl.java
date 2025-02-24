package com.maymar.rewards.program.service;

import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.dto.Transaction;
import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import com.maymar.rewards.program.exception.custom.NoTransactionsFoundException;
import com.maymar.rewards.program.repository.RewardsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    private RewardsResponseDto processAndPrepareResponse(List<CustomerTransactionsEntity> customerTransactions, String userId) {
        //To Group the Transactions By Month-Year pair
        Map<String, List<CustomerTransactionsEntity>> groupedByMonth = customerTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTranDate().getMonth().toString() +
                                "-" +
                                transaction.getTranDate().getYear()
                ));

        //To Get The total for each Month-Year pair
        Map<String, Integer> resultMap = new HashMap<>();
        List<Transaction> transactionList = new ArrayList<>();
        int rewardPoint;

        for (Map.Entry<String, List<CustomerTransactionsEntity>> entry : groupedByMonth.entrySet()){
            int monthlyRewards = 0;
            for (CustomerTransactionsEntity transaction : entry.getValue()){
                rewardPoint = rewardsCalculator(transaction);
                if (rewardPoint > 0){
                    monthlyRewards += rewardPoint;
                    transactionList.add(new Transaction(transaction.getTransactionId(),
                            transaction.getTranAmt(),
                            transaction.getTranDate()));
                }
            }
            resultMap.put(entry.getKey(), monthlyRewards);
        }

        return new RewardsResponseDto(userId,
                resultMap,
                resultMap.values().stream().mapToInt(Integer::intValue).sum(),
                transactionList,
                LocalDate.now()
        );
    }

    public int rewardsCalculator(CustomerTransactionsEntity transaction) {
        if (transaction.getTranAmt().compareTo(new BigDecimal(50)) > 0 &&
                transaction.getTranAmt().compareTo(new BigDecimal(100)) <= 0) {
            return transaction.getTranAmt().subtract(new BigDecimal(50)).intValue();
        } else if (transaction.getTranAmt().compareTo(new BigDecimal(100)) > 0){
            return ((transaction.getTranAmt().subtract(new BigDecimal(100)).intValue()) * 2) + 50;
        }else{
            return 0;
        }
    }
}
