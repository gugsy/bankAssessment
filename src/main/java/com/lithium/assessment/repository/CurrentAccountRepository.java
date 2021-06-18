package com.lithium.assessment.repository;

import com.lithium.assessment.entities.CurrentAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccountModel,Long> {
}
