package com.revature.bank.accounts;

public class Account {
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
	public boolean withdraw(double amount) {
		// TODO Implement
		return false;
	}
	
	/**
	 * Deposit the amount into the bank account.
	 * @param amount Not negative.
	 */
	public boolean deposit(double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Account to, double amount) {
		return false;
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
	
}
