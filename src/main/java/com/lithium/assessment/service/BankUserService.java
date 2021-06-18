package com.lithium.assessment.service;

import com.lithium.assessment.entities.BankUser;
import org.springframework.stereotype.Service;

@Service
public interface BankUserService {

    public BankUser getByEmail(String email);
}
