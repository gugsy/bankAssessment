package com.lithium.assessment.logic;

import com.lithium.assessment.entities.BankUser;
import org.springframework.beans.factory.annotation.Autowired;

public class SavingsAccount implements BankAccount{

    @Autowired
    private BankUser bankUser;

    @Override
    public void depositMoney(BankUser bankUser, double amount) {

    }

    @Override
    public void withdrawMoney(BankUser bankUser, double amount) {

    }

    @Override
    public void transferMoney(BankUser bankUser, double amount) {

    }

    @Override
    public void openAccount(BankUser bankUser, double amount) {

    }
}
