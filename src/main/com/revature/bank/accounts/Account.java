package com.revature.bank.accounts;

public class Account {
	private double amount;
	
	public Account() {
		amount = 0;
	}

	/**
	 * Withdraw the amount in the bank account.
	 * @param amount Not negative.
	 */
	public void withdraw(double amount) {
		// TODO Implement
	}
	
	/**
	 * Deposit the amount into the bank account.
	 * @param amount Not negative.
	 */
	public void deposit(double amount) {
		// TODO Implement
	}
	
	/**
	 * @return The amount currently in the account.
	 */
	public double getAmount() {
		return amount;
	}
	
}
