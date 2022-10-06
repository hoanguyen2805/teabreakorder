package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.BankingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingHistoryRepository extends JpaRepository<BankingHistory, Long> {
}
