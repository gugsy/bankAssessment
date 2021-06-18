package com.lithium.assessment.service;

import com.lithium.assessment.entities.BankUser;

public interface BankUserService {

    public BankUser getByEmail(String email);
}
