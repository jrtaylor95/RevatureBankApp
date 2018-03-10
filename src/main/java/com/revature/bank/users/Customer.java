package com.revature.bank.users;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revature.bank.accounts.Account;

public class Customer extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3179028426414747013L;
	private List<Account> accounts;
	{
		accounts = new ArrayList<>();
	}
	
	public static Customer parseCustomer(ResultSet set) {
		Customer customer = new Customer();
		
		return customer;
	}
	
	public Customer() {};
	
	public Customer(String userName, String password) {
		super(userName, password);
	}
	
	public List<Account> getAccounts() {
		return this.accounts;
	}
	
	public Account getAccount(int idx) throws IndexOutOfBoundsException {
		return accounts.get(idx);
	}
	
	public void addAccount(Account account) {
		this.accounts.add(account);
	}
	
	public void removeAccount(int idx) {
		this.accounts.remove(idx);
	}
	
	public String toString() {
		String result = String.format("Customer %s:\n"
				+ "Name: %s %s\n", this.getUserName(), this.getFirstName(), this.getLastName());
		for (Account account : accounts) {
			result += account.toString();
		}
		
		return result;
	}
}
