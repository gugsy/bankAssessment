package com.lithium.assessment.serviceImpl;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.repository.BankUserRepository;
import com.lithium.assessment.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BankUserServiceImpl implements BankUserService {
    @Autowired
    private BankUserRepository bankUserRepository;

    public BankUserServiceImpl(BankUserRepository bankUserRepository) {
        this.bankUserRepository = bankUserRepository;
    }

    @Override
    public BankUser getByEmail(String email) {
        return bankUserRepository.getByEmail(email);
    }
}
