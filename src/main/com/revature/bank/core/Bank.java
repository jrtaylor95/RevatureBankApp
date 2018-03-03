package com.revature.bank.core;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;
import com.revature.bank.users.User;

public class Bank {
	List<Customer> customers;
	List<Employee> employees;
	Queue<Account> pendingAccounts;
	{
		customers = new ArrayList<>();
		employees = new ArrayList<>();
		pendingAccounts = new LinkedList<>();
	}
	
	public User logon(String userName, String password) {
		return null;
	}
	
	public User register(String userName, String password) {
		return null;
	}
	
	
	
}
