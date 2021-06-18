package com.lithium.assessment.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

@Id
    private long accountNumber;
    private ACCOUNTTYPE savingsAccount;
    private ACCOUNTTYPE currentAccount;
    private Date openingDate;
    private Date transactionDate;
    private double currentBalance;
    private double depositAmount;
    private double withdrawnAmount;
    private double overdraftBalance;





}
