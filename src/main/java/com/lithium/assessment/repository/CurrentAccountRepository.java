package com.lithium.assessment.repository;

import com.lithium.assessment.entities.CurrentAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentAccountRepository extends JpaRepository<CurrentAccountModel,Long> {
}
