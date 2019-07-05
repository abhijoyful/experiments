package com.bank.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity bean representing bank account in database. 
 * @author Abhishek Malik
 */
@Entity
@Table(name = "account")
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Id
	@Column
	private int accountNumber;
	
	@Column
	private double accountBalance;
	
	public Account(int accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}
	
	public Account(int accountNumber, double initialBalance) {
		super();
		this.accountNumber = accountNumber;
		this.accountBalance = initialBalance;
	}
	
	public Account() { }

	/**
	 * getter for account balance.
	 * @return the balance
	 */
	public double getAccountBalance() {
		return accountBalance;
	}

	/**
	 * setter for account balance.
	 * @param accBalance the account balance to set
	 */
	public void setAccountBalance(double accBalance) {
		this.accountBalance = accBalance;
	}

	/**
	 * getter for account number.
	 * @return the account number
	 */
	public int getAccountNumber() {
		return this.accountNumber;
	}

	/**
	 * setter for account number.
	 * @param accountNum the account number to set
	 */
	public void setAccountNumber(int accountNum) {
		this.accountNumber = accountNum;
	}
	
}
