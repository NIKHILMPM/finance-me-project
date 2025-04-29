package com.project.staragile.banking;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "account_number")
    private int accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_balance")
    private double accountBalance;

    public Account() {
    }

    public Account(int accountNumber, String accountName, String accountType, double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    // Getters and Setters
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
