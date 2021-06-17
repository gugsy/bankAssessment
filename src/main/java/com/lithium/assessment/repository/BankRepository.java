package com.lithium.assessment.repository;

import com.lithium.assessment.entities.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankUser,String> {
}
