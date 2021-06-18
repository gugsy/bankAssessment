package com.lithium.assessment.serviceImpl;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.repository.BankRepository;
import com.lithium.assessment.service.BankUserService;

public class BankUserServiceImpl implements BankUserService {

    private BankRepository bankRepository;

    public BankUserServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public BankUser getByEmail(String email) {
        return bankRepository.getByEmail(email);
    }
}
