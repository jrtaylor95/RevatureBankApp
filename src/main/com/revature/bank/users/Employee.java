package com.revature.bank.users;

import java.util.List;

import com.revature.bank.accounts.Account;

public class Employee extends User {

	public Employee(String userName, String password) {
		super(userName, password);
	}
	
	public void checkAccountInformation(Account account) {
		
	}
	
	public void checkAccountBalance(Account account) {
		
	}
	
	public void checkCustomerInformation(Customer customer) {
		
	}
	
	public List<Account> approveDenyApplication(List<Account> accounts, Account account) {
		return null;
	}

}
