package com.lithium.assessment;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.repository.BankRepository;

public class Bank {

   private  BankRepository bankRepository;

    public Bank(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public void adduser(BankUser user) {
        bankRepository.save(user);
    }
}
