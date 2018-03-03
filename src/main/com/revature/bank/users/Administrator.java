package com.revature.bank.users;

import java.util.List;

import com.revature.bank.accounts.Account;

public class Administrator extends Employee {

	public Administrator(String userName, String password) {
		super(userName, password);
	}

	public boolean withdraw(Account account) {
		return false;
	}
	
	public boolean deposit(Account account) {
		return false;
	}
	
	public boolean transfer(Account from, Account to) {
		return false;
	}
	
	public List<Account> cancelAccount(List<Account> accounts, Account account) {
		return null;
	}
}
