package com.revature.bank.users;

import java.util.ArrayList;
import java.util.List;

import com.revature.bank.accounts.Account;

public class Customer extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3179028426414747013L;
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
	
	public Account getAccount(int idx) {
		return accounts.get(idx);
	}
	
	public void addAccount(Account account) {
		this.accounts.add(account);
	}
	
	public boolean withdraw(int idx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean deposit(int idx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(int fromIdx, Customer toCustomer, int toIdx, double amount) {
		// TODO Implement
		return false;
	}
}
