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

public class SavingsAccountModel {

    @Id
    private long accountSavingsNumber;
    private LocalDate openingDate;
    private LocalDate transactionDate;
    private double currentBalance;
    private double depositAmount;
    private double withdrawnAmount;
    private double overdraftBalance;
    private double minimumDeposit;
    private  double minimumBalance;
    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email", nullable = false)
    private BankUser bankUser;
*/

}
