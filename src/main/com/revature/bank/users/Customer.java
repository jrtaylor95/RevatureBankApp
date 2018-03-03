package com.revature.bank.users;

import java.util.ArrayList;
import java.util.List;

import com.revature.bank.accounts.Account;

public class Customer extends User {
	List<Account> accounts;
	{
		accounts = new ArrayList<>();
	}
	
	public Customer(String userName, String password) {
		super(userName, password);
	}
	
	public List<Account> getAccounts() {
		return this.accounts;
	}
	
	public void addAccount(Account account) {
		this.accounts.add(account);
	}
}
