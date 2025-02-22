package com.maymar.rewards.program.repository;

import com.maymar.rewards.program.entity.CustomerTransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RewardsRepository extends JpaRepository<CustomerTransactionsEntity, Long> {

    Optional<List<CustomerTransactionsEntity>> findByUserId(String userId);

    @Query("SELECT t FROM CustomerTransactionsEntity t WHERE t.userId = ?1 AND t.tranDate BETWEEN ?2 AND ?3")
    Optional<List<CustomerTransactionsEntity>> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);
}
