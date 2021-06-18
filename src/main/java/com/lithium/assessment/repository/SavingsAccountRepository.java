package com.lithium.assessment.repository;

import com.lithium.assessment.entities.SavingsAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccountModel, Long> {

}
