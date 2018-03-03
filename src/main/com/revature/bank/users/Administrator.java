package com.revature.bank.users;

import java.util.List;

import com.revature.bank.accounts.Account;

public class Administrator extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4607580623653880109L;

	public Administrator(String userName, String password) {
		super(userName, password);
	}

	public boolean withdraw(Account account) {
		// TODO Implement
		return false;
	}
	
	public boolean deposit(Account account) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Account from, Account to) {
		// TODO Implement
		return false;
	}
	
	public List<Account> cancelAccount(List<Account> accounts, Account account) {
		// TODO Implement
		return null;
	}
}
