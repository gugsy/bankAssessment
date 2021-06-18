package com.lithium.assessment.repository;

import com.lithium.assessment.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
