package com.revature.bank.core;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Administrator;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;
import com.revature.bank.users.User;

public class Bank {
	private Map<String, Customer> customers;
	private Map<String, Employee> employees;
	private Map<Customer, Account> pendingAccounts;
	{
		customers = new HashMap<>();
		employees = new HashMap<>();
		pendingAccounts = new TreeMap<>();
	}
	
	public Collection<Customer> getCustomers() {
		return customers.values();
	}
	
	public Collection<Employee> getEmployees() {
		return employees.values();
	}
	
	public Set<Entry<Customer, Account>> getPendingAccounts() {
		return pendingAccounts.entrySet();
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
	
	public boolean withdraw(Customer customer, int accountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean withdraw(Administrator admin, String customer, int accountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean deposit(Customer customer, int accountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean deposit(Administrator admin, String customer, int accountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Customer customer, int fromAccountIdx, int toAccountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Customer fromCustomer, int fromAccountIdx, String toCustomer, int toAccountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Administrator admin, String customer, int fromAccountIdx, int toAccountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean transfer(Administrator admin, String fromCustomer, int fromAccountIdx, String toCustomer, int toAccountIdx, double amount) {
		// TODO Implement
		return false;
	}
	
	public boolean cancelAccount(Administrator admin, String customer) {
		// TODO Implement
		return false;
	}
	
	public void viewCustomerInformation(Employee employee, String customer) {
		// TODO Implement
	}
	
	public void approveAccount(String customer) {
		
	}
	
	public void rejectAccount(String customer) {
		
	}
}
