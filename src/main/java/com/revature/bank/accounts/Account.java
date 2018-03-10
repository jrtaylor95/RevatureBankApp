package com.revature.bank.accounts;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -898679925211200313L;
	private String nickName;
	private double balance;
	{
		balance = 0;
	}
	
	public Account(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * Withdraw the amount in the bank account.
	 * @param amount Not negative.
	 */
	public boolean withdraw(double amount) throws IllegalArgumentException {
		if (amount <= 0 || amount > balance)
			throw new IllegalArgumentException();
		this.balance -= amount;
		
		return true;
	}
	
	/**
	 * Deposit the amount into the bank account.
	 * @param amount Not negative.
	 */
	public boolean deposit(double amount) throws IllegalArgumentException {
		if (amount <= 0)
			throw new IllegalArgumentException();
		this.balance += amount;
		
		return true;
	}
	
	/**
	 * @return The amount currently in the account.
	 */
	public double getBalance() {
		return balance;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String toString() {
		return String.format("Account %s:\nBalance: $.2f\n", nickName, balance);
	}
}
