package com.revature.bank.core;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;
import com.revature.bank.users.User;

public class Bank {
	Map<String, Customer> customers;
	Map<String, Employee> employees;
	Map<Customer, Account> pendingAccounts;
	{
		customers = new HashMap<>();
		employees = new HashMap<>();
		pendingAccounts = new TreeMap<>();
	}
	
	public User logon(String userName, String password) {
		// TODO Implement
		return null;
	}
	
	public User register(String userName, String password) {
		// TODO Implement
		return null;
	}
	
	public void apply(Customer customer) {
		// TODO Implement
	}
	
	public boolean withdraw(Account account, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean deposit(Account account, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Account from, Account to, double amount) {
		// TODO Implement
		return false;
	}
}
