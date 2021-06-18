package com.lithium.assessment.repository;

import com.lithium.assessment.entities.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface BankUserRepository extends JpaRepository<BankUser,String> {

    @Query("FROM CustomerPersonalDetails WHERE licenseIDNumber = ?1")
    BankUser getByEmail(String email);
}
