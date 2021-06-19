package com.lithium.assessment.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TransactionDetails {
    @Id
    @Column(name="transactionId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;
    @Column(name ="email")
    private String email;
    @Column(name="transactionDate")
    private LocalDate localDate;
    @Column (name="accountType")
    private ACCOUNTTYPE accountType;
    @Column (name ="accountNumber")
    private long accountNumber;
    @Column(name="openingBalance")
    private double openingBalance;
    @Column(name = "withdrawal")
    private double withdrawal;
    @Column(name="deposit")
    private double deposit;
    @Column(name = "balance")
    private double balance;

}
