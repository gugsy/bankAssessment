package com.lithium.assessment.logic;

import com.lithium.assessment.entities.BankUser;

public interface BankAccount {

    void depositMoney(BankUser bankUser,double amount);
    void withdrawMoney(BankUser bankUser,double amount);
    void transferMoney(BankUser bankUser,double amount);
    void openAccount(BankUser bankUser,double amount);

}
