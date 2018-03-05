package com.revature.bank.core;
import java.io.Serializable;
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
import com.revature.util.LoggingUtil;

public class Bank implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1647119949555847865L;
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
		User user = null;
		
		if (userName == null || password == null)
			throw new NullPointerException();
		
		if (customers.containsKey(userName)) {
			user = customers.get(userName);
		} else if (employees.containsKey(userName)) {
			user = employees.get(userName);
		}
		
		if (user != null) {
			if (user.checkPassword(password)) {
				LoggingUtil.logDebug(String.format("LOGON: Successful log in attempt for user %s", userName));
				return user;
			} else {
				LoggingUtil.logDebug(String.format("LOGON: Unsuccessful log in attempt for user %s", userName));
				return null;
			}
		} else
			return null;
		
		
	}
	
	public User register(String userName, String password) {
		if (userName == null || password == null)
			throw new NullPointerException();
		
		if (customers.containsKey(userName)) {
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));
			return null;
		} else if (employees.containsKey(userName)) {
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));
			return null;
		}
		
		Customer customer = new Customer(userName, password);
		customers.put(userName, customer);
		LoggingUtil.logDebug(String.format("REGISTER: Successful registration for user %s", userName));
		
		return customer;
	}
	
	public User registerEmployee(String userName, String password, boolean isAdmin) {
		if (userName == null || password == null)
			throw new NullPointerException();
		
		if (customers.containsKey(userName)) {
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));
			return null;
		} else if (employees.containsKey(userName)) {
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));
			return null;
		}
		
		Employee employee = null;
		if (isAdmin) {
			employee = new Employee(userName, password);
		} else {
			employee = new Administrator(userName, password);
		}
		employees.put(userName, employee);
		
		LoggingUtil.logDebug(String.format("REGISTER: Successful registration for user %s", userName));
		
		return employee;
	}
	
	public void apply(Customer customer, String accountName) {
		Account account = new Account(accountName);
		pendingAccounts.put(customer.getUserName(), account);
		LoggingUtil.logDebug(String.format("ACCOUNT_APPLICATION: User %s applied for account %s", customer.getUserName(), accountName));
	}
	
	public boolean withdraw(Customer customer, int accountIdx, double amount) throws IllegalArgumentException {
		Account account = customer.getAccount(accountIdx);
		boolean wasSuccessful = account.withdraw(amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("WITHDRAW: Customer %s successfully withdrawed $%.2f from account %s",
					customer.getUserName(), amount, account.getNickName()));
		else
			LoggingUtil.logDebug(String.format("WITHDRAW: Customer %s unsuccessfully tried to withdraw $%.2f from account %s",
					customer.getUserName(), amount, account.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean withdraw(Administrator admin, String customerUserName, int accountIdx, double amount) {
		Customer customer = findCustomer(customerUserName);
		
		if (customer == null)
			return false;
		
		Account account = customer.getAccount(accountIdx);
		boolean wasSuccessful = account.withdraw(amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("WITHDRAW: Administrator %s successfully withdrawed $%.2f from user %s account %s",
					admin.getUserName(), amount, customerUserName, account.getNickName()));
		else
			LoggingUtil.logDebug(String.format("WITHDRAW: Administrator %s unsuccessfully tried to withdraw $%.2f from user %s account %s",
					admin.getUserName(), amount, customerUserName, account.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean deposit(Customer customer, int accountIdx, double amount) {
		Account account = customer.getAccount(accountIdx);
		boolean wasSuccessful = account.deposit(amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("DEPOSIT: Customer %s successfully deposited $%.2f to account %s",
					customer.getUserName(), amount, account.getNickName()));
		else
			LoggingUtil.logDebug(String.format("DEPOSIT: Customer %s unsuccessfully tried to deposit $%.2f to account %s",
					customer.getUserName(), amount, account.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean deposit(Administrator admin, String customerUserName, int accountIdx, double amount) {
		Customer customer = findCustomer(customerUserName);
		
		if (customer == null)
			return false;
		
		Account account = customer.getAccount(accountIdx);
		boolean wasSuccessful = account.deposit(amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("DEPOSIT: Administrator %s successfully deposited $%.2f to user %s account %s",
					admin.getUserName(), amount, customerUserName, account.getNickName()));
		else
			LoggingUtil.logDebug(String.format("DEPOSIT: Administrator %s unsuccessfully tried to deposit $%.2f to user %s account %s",
					admin.getUserName(), amount, customerUserName, account.getNickName()));
		
		return wasSuccessful;
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
		boolean wasSuccessful = transfer(fromAccount, toAccount, amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("TRANSFER: User %s successfully transfered $%.2f from account %s to account %s",
					customer.getUserName(), amount, fromAccount.getNickName(), toAccount.getNickName()));
		else
			LoggingUtil.logDebug(String.format("TRANSFER: User %s unsuccessfully tried to transfer $%.2f from account %s to account %s",
					customer.getUserName(), amount, fromAccount.getNickName(), toAccount.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean transfer(Customer fromCustomer, int fromAccountIdx, String toCustomerUserName, int toAccountIdx, double amount) throws IllegalArgumentException {
		Customer toCustomer = findCustomer(toCustomerUserName);
		
		if (toCustomer == null)
			return false;
		
		Account fromAccount = fromCustomer.getAccount(fromAccountIdx);
		Account toAccount = toCustomer.getAccount(toAccountIdx);
		boolean wasSuccessful = transfer(fromAccount, toAccount, amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("TRANSFER: User %s successfully transfered $%.2f from account %s to user %s account %s",
					fromCustomer.getUserName(), amount, fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName()));
		else
			LoggingUtil.logDebug(String.format("TRANSFER: User %s unsuccessfully tried to transfer $%.2f from account %s to user %s account %s",
					fromCustomer.getUserName(), amount, fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean transfer(Administrator admin, String customerUserName, int fromAccountIdx, int toAccountIdx, double amount) {
		Customer customer = findCustomer(customerUserName);
		
		if (customer == null)
			return false;
		
		Account fromAccount = customer.getAccount(fromAccountIdx);
		Account toAccount = customer.getAccount(toAccountIdx);
		boolean wasSuccessful = transfer(fromAccount, toAccount, amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s successfully transfered $%.2f from user %s account %s to account %s",
					admin.getUserName(), amount, customerUserName, fromAccount.getNickName(), toAccount.getNickName()));
		else
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s unsuccessfully tried to transfered $%.2f from user %s account %s to account %s",
					admin.getUserName(), amount, customerUserName, fromAccount.getNickName(), toAccount.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean transfer(Administrator admin, String fromCustomerUserName, int fromAccountIdx, String toCustomerUserName, int toAccountIdx, double amount) {
		Customer fromCustomer = findCustomer(fromCustomerUserName);
		Customer toCustomer = findCustomer(toCustomerUserName);
		
		if (fromCustomer == null || toCustomer == null)
			return false;
		
		Account fromAccount = fromCustomer.getAccount(fromAccountIdx);
		Account toAccount = toCustomer.getAccount(toAccountIdx);
		boolean wasSuccessful = transfer(fromAccount, toAccount, amount);
		
		if (wasSuccessful)
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s successfully transfered $%.2f from user %s account %s to user %s account %s",
					admin.getUserName(), amount, fromCustomerUserName, fromAccount.getNickName(), toCustomerUserName, toAccount.getNickName()));
		else
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s unsuccessfully tried to transfered $%.2f from user %s account %s to user %s account %s",
					admin.getUserName(), amount, fromCustomerUserName, fromAccount.getNickName(), toCustomerUserName, toAccount.getNickName()));
		
		return wasSuccessful;
	}
	
	public boolean cancelAccount(Administrator admin, String customerUserName, int accountIdx) {
		if (customerUserName == null || admin == null)
			throw new NullPointerException();
		
		Customer customer = findCustomer(customerUserName);
		Account account = customer.getAccount(accountIdx);
		customer.removeAccount(accountIdx);
		
		LoggingUtil.logDebug(String.format("CANCEL_ACCOUNT: Adminsitrator %s canceled user %s account %s",
				admin.getUserName(), customerUserName, account.getNickName()));
		
		return true;
	}
	
	public String getCustomerInformation(Employee employee, String customerUserName) {
		Customer customer = findCustomer(customerUserName);
		
		return customer.toString();
	}
	
	public boolean approveAccount(Employee employee, String customerUserName) {
		if (customerUserName == null)
			throw new NullPointerException();
		
		Account account = pendingAccounts.remove(customerUserName);
		if (account == null)
			return false;
		
		Customer customer = findCustomer(customerUserName);
		if (customer == null)
			return false;
		
		customer.addAccount(account);
		
		LoggingUtil.logDebug(String.format("APPROVE_ACCOUNT: Employee %s approved user %s account %s",
				employee.getUserName(), customerUserName, account.getNickName()));
		
		return false;
	}
	
	public boolean rejectAccount(Employee employee, String customerUserName) {
		if (customerUserName == null)
			throw new NullPointerException();
		
		Account account = pendingAccounts.remove(customerUserName);
		if (account == null)
			return false;
		
		LoggingUtil.logDebug(String.format("REJECT_ACCOUNT: Employee %s rejected user %s account %s",
				employee.getUserName(), customerUserName, account.getNickName()));
		
		return true;
	}
}
