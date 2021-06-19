package com.lithium.assessment.repository;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.entities.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionDetails,Long> {
    @Query("FROM TransactionDetails WHERE email = ?1")
    List<TransactionDetails> getByEmail(String email);

}
