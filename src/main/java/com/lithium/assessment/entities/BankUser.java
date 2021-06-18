package com.lithium.assessment.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "bankUser")
public class BankUser {

    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "dob")
    private Date dob;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accountSavingsNumber", nullable = false)
    private SavingsAccountModel savingsAccountModel;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="accountCurrentNumber",nullable =false)
    private CurrentAccountModel currentAccountModel;

}
