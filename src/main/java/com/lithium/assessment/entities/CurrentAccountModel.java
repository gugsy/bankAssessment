package com.lithium.assessment.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "currentAccountModel")
public class CurrentAccountModel {

    @Id
    @Column(name = "accountCurrentNumber")
    private long accountCurrentNumber;
    @Column(name = "openingDate")
    private LocalDate openingDate;
    @Column(name = "transactionDate")
    private LocalDate transactionDate;
    @Column(name = "currentBalance")
    private double currentBalance;
    @Column(name = "depositAmount")
    private double depositAmount;
    @Column(name = "withdrawnAmount")
    private double withdrawnAmount;
    @Column(name = "overdraftBalance")
    private double overdraftBalance;
    @Column(name = "maximumOverdraft")
    private double maximumOverdraft;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "email", nullable = false)
//    private BankUser bankUser;

}
