package com.lithium.assessment.repository;

import com.lithium.assessment.entities.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BankUserRepository extends JpaRepository<BankUser,String> {

    @Query("FROM BankUser WHERE email = ?1")
    BankUser getByEmail(String email);
}
