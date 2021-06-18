package com.lithium.assessment.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "savingsAccountModel")
public class SavingsAccountModel {

    @Id
    @Column(name = "accountSavingsNumber")
    private long accountSavingsNumber;
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
    @Column(name = "minimumDeposit")
    private double minimumDeposit;
    @Column(name = "minimumBalance")
    private  double minimumBalance;
    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email", nullable = false)
    private BankUser bankUser;
*/

}
