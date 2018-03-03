package com.revature.bank.accounts;

public class Account {
	private double balance;
	
	public Account() {
		balance = 0;
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
	
	public boolean transfer(double amount, Account account) {
		return false;
	}
	
	/**
	 * @return The amount currently in the account.
	 */
	public double getBalance() {
		return balance;
	}
	
}
