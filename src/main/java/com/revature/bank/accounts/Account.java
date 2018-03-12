package com.revature.bank.accounts;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account implements Serializable {
	
	private static final long serialVersionUID = -898679925211200313L;
	private int id;
	private String nickName;
	private double balance;
	{
		balance = 0;
	}
	
	public static Account parseAccount(ResultSet set) throws SQLException {
		Account account = new Account();
		
		account.setId(set.getInt(1));
		account.setNickName(set.getString(2));
		account.setBalance(set.getDouble(3));
		
		return account;
	}
	
	public Account() {}
	
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
	
	private void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	private void setBalance(double balance) {
		this.balance = balance;
	}
	
}
