package com.revature.bank.users;

import java.util.List;

import com.revature.bank.accounts.Account;

public class Employee extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2468531644845540457L;

	public Employee(String userName, String password) {
		super(userName, password);
	}
	
	public void checkAccountInformation(Account account) {
		// TODO Implement
	}
	
	public void checkAccountBalances(Customer customer) {
		// TODO Implement
	}
	
	public void checkCustomerInformation(Customer customer) {
		// TODO Implement
	}
	
	public List<Account> approveDenyApplication(List<Account> accounts, Account account) {
		// TODO Implement
		return null;
	}

}
