package com.lithium.assessment.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
public class BankUser {


    private String name;
    private String surname;
    @Id
    private String email;
    private ACCOUNTTYPE savingsaccount;
    private ACCOUNTTYPE currentaccount;

    public BankUser(String name, String surname, String email, ACCOUNTTYPE savingsaccount, ACCOUNTTYPE currentaccount) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.savingsaccount = savingsaccount;
        this.currentaccount = currentaccount;
    }
}
