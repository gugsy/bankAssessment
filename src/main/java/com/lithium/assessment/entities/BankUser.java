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

public class BankUser {

    @Id
    private String email;
    private String name;
    private String surname;
    private Date dob;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountNumber", nullable = false)
    private SavingsAccountModel savingsAccountModel;
    @OneToOne
    @JoinColumn(name="accountCurrentNumber",nullable =false)
    private CurrentAccountModel currentAccountModel;

}
