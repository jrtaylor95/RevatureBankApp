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
	private Map<String, Account> pendingAccounts;
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
	
	public Set<Entry<String, Account>> getPendingAccounts() {
		return pendingAccounts.entrySet();
	}
	
	public User logon(String userName, String password) {
		if (userName == null || password == null)
			throw new NullPointerException();
		
		if (!customers.containsKey(userName))
			return null;
		
		Customer customer = customers.get(userName);
		if (customer.checkPassword(password))
			return customer;
		else
			return null;
	}
	
	public User register(String userName, String password) {
		if (userName == null || password == null)
			throw new NullPointerException();
		
		if (customers.containsKey(userName))
			return null;
		
		Customer customer = new Customer(userName, password);
		customers.put(userName, customer);
		
		return customer;
	}
	
	public void apply(Customer customer, String accountName) {
		Account account = new Account(accountName);
		pendingAccounts.put(customer.getUserName(), account);
	}
	
	public boolean withdraw(Customer customer, int accountIdx, double amount) throws IllegalArgumentException {
		return customer.getAccount(accountIdx).withdraw(amount);
	}
	
	public boolean withdraw(Administrator admin, String customerUserName, int accountIdx, double amount) {
		Customer customer = customers.get(customerUserName);
		
		return customer.getAccount(accountIdx).withdraw(amount);
	}
	
	public boolean deposit(Customer customer, int accountIdx, double amount) {
		return customer.getAccount(accountIdx).deposit(amount);
	}
	
	public boolean deposit(Administrator admin, String customerUserName, int accountIdx, double amount) {
		Customer customer = findCustomer(customerUserName);
		
		if (customer == null)
			return false;
		
		return customer.getAccount(accountIdx).deposit(amount);
	}
	
	private boolean transfer(Account fromAccount, Account toAccount, double amount) throws IllegalArgumentException {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
		
		return true;
	}
	
	private Customer findCustomer(String userName) throws NullPointerException {
		if (userName == null)
			throw new NullPointerException();
		
		return customers.get(userName);
	}
	
	public boolean transfer(Customer customer, int fromAccountIdx, int toAccountIdx, double amount) {
		Account fromAccount = customer.getAccount(fromAccountIdx);
		Account toAccount = customer.getAccount(toAccountIdx);
		
		return transfer(fromAccount, toAccount, amount);
	}
	
	public boolean transfer(Customer fromCustomer, int fromAccountIdx, String toCustomerUserName, int toAccountIdx, double amount) throws IllegalArgumentException {
		Customer toCustomer = findCustomer(toCustomerUserName);
		
		if (toCustomer == null)
			return false;
		
		Account fromAccount = fromCustomer.getAccount(fromAccountIdx);
		Account toAccount = toCustomer.getAccount(toAccountIdx);
		return transfer(fromAccount, toAccount, amount);
	}
	
	public boolean transfer(Administrator admin, String customerUserName, int fromAccountIdx, int toAccountIdx, double amount) {
		Customer customer = findCustomer(customerUserName);
		
		if (customer == null)
			return false;
		
		Account fromAccount = customer.getAccount(fromAccountIdx);
		Account toAccount = customer.getAccount(toAccountIdx);
		return transfer(fromAccount, toAccount, amount);
	}
	
	public boolean transfer(Administrator admin, String fromCustomerUserName, int fromAccountIdx, String toCustomerUserName, int toAccountIdx, double amount) {
		Customer fromCustomer = findCustomer(fromCustomerUserName);
		Customer toCustomer = findCustomer(toCustomerUserName);
		
		if (fromCustomer == null || toCustomer == null)
			return false;
		
		Account fromAccount = fromCustomer.getAccount(fromAccountIdx);
		Account toAccount = toCustomer.getAccount(toAccountIdx);
		return transfer(fromAccount, toAccount, amount);
	}
	
	public boolean cancelAccount(Administrator admin, String customerUserName, int accountIdx) {
		if (customerUserName == null || admin == null)
			throw new NullPointerException();
		
		Customer customer = findCustomer(customerUserName);
		customer.removeAccount(accountIdx);
		
		return true;
	}
	
	public void viewCustomerInformation(Employee employee, String customer) {
		// TODO Implement
	}
	
	public boolean approveAccount(String customerUserName) {
		if (customerUserName == null)
			throw new NullPointerException();
		
		Account account = pendingAccounts.remove(customerUserName);
		if (account == null)
			return false;
		
		Customer customer = findCustomer(customerUserName);
		if (customer == null)
			return false;
		
		customer.addAccount(account);
		
		return false;
	}
	
	public boolean rejectAccount(String customerUserName) {
		if (customerUserName == null)
			throw new NullPointerException();
		
		Account account = pendingAccounts.remove(customerUserName);
		if (account == null)
			return false;
		
		return true;
	}
}
