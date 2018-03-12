package com.revature.bank.core;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import com.revature.bank.accounts.Account;
import com.revature.bank.database.dao.AccountDataAccessObject;
import com.revature.bank.database.dao.CustomerDataAccessObject;
import com.revature.bank.database.dao.EmployeeDataAccessObject;
import com.revature.bank.users.Administrator;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;
import com.revature.bank.users.User;
import com.revature.util.ConnectionFactory;
import com.revature.util.LoggingUtil;

public class Bank implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1647119949555847865L;
	private Connection connection;
	private CustomerDataAccessObject customerDao;
	private EmployeeDataAccessObject employeeDao;
	private AccountDataAccessObject accountDao;
	{
		connection = ConnectionFactory.getInstance().getConnection();
		customerDao = new CustomerDataAccessObject(connection);
		employeeDao = new EmployeeDataAccessObject(connection);
		accountDao = new  AccountDataAccessObject(connection);
	}

	public Customer getCustomer(String userName) throws SQLException {
		return customerDao.select(userName);
	}
	
	public List<Customer> getCustomers() throws SQLException {
		return customerDao.selectAll();
	}

	public List<Employee> getEmployees() throws SQLException {
		return employeeDao.selectAll();
	}

	public List<Account> getPendingAccounts(Customer customer) throws SQLException {
		return accountDao.selectPending(customer.getUserName());
	}
	
	public List<Account> getAccounts(Customer customer) throws SQLException {
		return accountDao.select(customer.getUserName());
	}

	/**
	 * Acts as a way for a user to log in to the bank
	 * @param userName
	 * @param password
	 * @return The user of the corresponding userName
	 * @throws SQLException 
	 */
	public User logon(String userName, String password) throws SQLException {
		User user = null;

		if (userName == null || password == null)
			throw new NullPointerException();

		String sql = "SELECT USER_NAME, PASSWORD FROM USER_LOGON WHERE USER_NAME = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		ResultSet set = statement.executeQuery();

		if (!set.next())
			return null;

		String actualPassword = set.getString(2);

		if (!password.equals(actualPassword)) {
			LoggingUtil.logDebug(String.format("LOGON: Unsuccessful log in attempt for user %s", userName));
			return null;
		}

		user = customerDao.select(userName);

		if (user == null)
			user = employeeDao.select(userName);
		LoggingUtil.logDebug(String.format("LOGON: Successful log in attempt for user %s", userName));
		return user;

	}

	/**
	 * Acts as a way to register a user to the bank
	 * @param userName
	 * @param password
	 * @return The user of the corresponding userName
	 * @throws SQLException 
	 */
	public User register(String userName, String password, String firstName, String lastName) throws SQLException {
		if (userName == null || password == null)
			throw new NullPointerException();

		CallableStatement statement = connection.prepareCall("{? = call IS_DUPLICATE_USER_NAME(?)}");
		statement.registerOutParameter(1, Types.SMALLINT);
		statement.setString(2, userName);
		statement.executeUpdate();
		boolean isDuplicate = statement.getShort(1) == 1 ? true : false;


		if (isDuplicate)
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));

		Customer customer = new Customer(userName, password);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);

		try {
			customerDao.create(customer);
			LoggingUtil.logDebug(String.format("REGISTER: Successful registration for user %s", userName));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("REGISTER: Unsuccessful registration for user %s (%s)", userName, e.getMessage()));
			throw e;
		}

		return customer;
	}

	/**
	 * Acts as a way to register an employee to the bank
	 * @param userName
	 * @param password
	 * @param isAdmin
	 * @return The user of the corresponding userName
	 * @throws SQLException 
	 */
	public User registerEmployee(String userName, String password, boolean isAdmin, String firstName, String lastName) throws SQLException {
		if (userName == null || password == null)
			throw new NullPointerException();

		CallableStatement statement = connection.prepareCall("{? = call IS_DUPLICATE_USER_NAME(?)}");
		statement.registerOutParameter(1, Types.SMALLINT);
		statement.setString(2, userName);
		statement.executeUpdate();
		boolean isDuplicate = statement.getShort(1) == 1 ? true : false;

		if (isDuplicate)
			LoggingUtil.logError(String.format("REGISTER: Duplicate username %s", userName));

		Employee employee = null;
		if (!isAdmin) {
			employee = new Employee(userName, password);
		} else {
			employee = new Administrator(userName, password);
		}
		employee.setFirstName(firstName);
		employee.setLastName(lastName);

		try {
			employeeDao.create(employee);
			LoggingUtil.logDebug(String.format("REGISTER: Successful registration for user %s", userName));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("REGISTER: Unsuccessful registration for user %s (%s)", userName, e.getMessage()));
			throw e;
		}

		return employee;
	}

	/**
	 * Allows a user to apply for an account with the nickName accountName
	 * @param customer
	 * @param accountName
	 */
	public void apply(Customer customer, String accountName) {
		Account account = new Account(accountName);
		try {
			accountDao.create(account, customer);
			LoggingUtil.logDebug(String.format("ACCOUNT_APPLICATION: User %s successfully applied for account %s",
					customer.getUserName(), accountName));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("ACCOUNT_APPLICATION: User %s unsuccessfully tried to apply for account %s (%s)",
					customer.getUserName(), accountName, e.getMessage()));
		}
	}

	public void applyJoint(Customer customer, String jointCustomerUserName, String accountName) {
		Account account = new Account(accountName);
		try {
			accountDao.create(account, customer);
			LoggingUtil.logDebug(String.format("ACCOUNT_APPLICATION: User %s successfully applied for account %s",
					customer.getUserName(), accountName));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("ACCOUNT_APPLICATION: User %s unsuccessfully tried to apply for account %s (%s)",
					customer.getUserName(), accountName, e.getMessage()));
		}
	}

	private void withdraw(Account account, double amount) throws SQLException {
		account.withdraw(amount);
		connection.setAutoCommit(false);
		accountDao.update(account);
		// TODO Log Transaction
		connection.commit();
		connection.setAutoCommit(true);
	}

	public void withdraw(Customer customer, Account account, double amount) {
		try {
			withdraw(account, amount);
			LoggingUtil.logDebug(String.format("WITHDRAW: Customer %s successfully withdrawed $%.2f from account %s",
					customer.getUserName(), amount, account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("WITHDRAW: Customer %s unsuccessfully tried to withdraw $%.2f from account %s (%s)",
					customer.getUserName(), amount, account.getNickName(), e.getMessage()));
		}
	}

	public void withdraw(Administrator admin, Customer customer, Account account, double amount) {
		try {
			withdraw(account, amount);
			LoggingUtil.logDebug(String.format("WITHDRAW: Administrator %s successfully withdrawed $%.2f from user %s account %s",
					admin.getUserName(), amount, customer.getUserName(), account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("WITHDRAW: Administrator %s unsuccessfully tried to withdraw $%.2f from user %s account %s (%s)",
					admin.getUserName(), amount, customer.getUserName(), account.getNickName(), e.getMessage()));
		}
	}


	private void deposit(Account account, double amount) throws SQLException {
		account.deposit(amount);
		connection.setAutoCommit(false);
		accountDao.update(account);
		// TODO Log Transaction
		connection.commit();
		connection.setAutoCommit(true);
	}

	public void deposit(Customer customer, Account account, double amount) {
		try {
			deposit(account, amount);
			LoggingUtil.logDebug(String.format("DEPOSIT: Customer %s successfully deposited $%.2f to account %s",
					customer.getUserName(), amount, account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("DEPOSIT: Customer %s unsuccessfully tried to deposit $%.2f to account %s (%s)",
					customer.getUserName(), amount, account.getNickName(), e.getMessage()));
		}
	}

	public void deposit(Administrator admin, Customer customer, Account account, double amount) {
		try {
			deposit(account, amount);
			LoggingUtil.logDebug(String.format("DEPOSIT: Administrator %s successfully deposited $%.2f to user %s account %s",
					admin.getUserName(), amount, customer.getUserName(), account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("DEPOSIT: Administrator %s unsuccessfully tried to deposit $%.2f to user %s account %s (%)",
					admin.getUserName(), amount, customer.getUserName(), account.getNickName(), e.getMessage()));
		}
	}

	private void transfer(Account fromAccount, Account toAccount, double amount) throws SQLException {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
		connection.setAutoCommit(false);
		accountDao.update(fromAccount);
		accountDao.update(toAccount);
		// TODO Log Transaction
		connection.commit();
		connection.setAutoCommit(true);
	}

	public void transfer(Customer customer, Account fromAccount, Account toAccount, double amount) {
		try {
			transfer(fromAccount, toAccount, amount);
			LoggingUtil.logDebug(String.format("TRANSFER: User %s successfully transfered $%.2f from account %s to account %s",
					customer.getUserName(), amount, fromAccount.getNickName(), toAccount.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("TRANSFER: User %s unsuccessfully tried to transfer $%.2f from account %s to account %s (%s)",
					customer.getUserName(), amount, fromAccount.getNickName(), toAccount.getNickName(), e.getMessage()));
		}
	}

	public void transfer(Administrator admin, Customer customer, Account fromAccount, Account toAccount, double amount) {
		try {
			transfer(fromAccount, toAccount, amount);
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s successfully transfered $%.2f from user %s account %s to account %s",
					admin.getUserName(), amount, customer.getUserName(), fromAccount.getNickName(), toAccount.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s unsuccessfully tried to transfered $%.2f from user %s account %s to account %s (%s)",
					admin.getUserName(), amount, customer.getUserName(), fromAccount.getNickName(), toAccount.getNickName(), e.getMessage()));
		}
	}

	public void transfer(Customer fromCustomer, Account fromAccount, Customer toCustomer, Account toAccount, double amount) {
		try {
			transfer(fromAccount, toAccount, amount);
			LoggingUtil.logDebug(String.format("TRANSFER: User %s successfully transfered $%.2f from account %s to user %s account %s",
					fromCustomer.getUserName(), amount, fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("TRANSFER: User %s unsuccessfully tried to transfer $%.2f from account %s to user %s account %s (%s)",
					fromCustomer.getUserName(), amount, fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName(), e.getMessage()));
		}
	}

	public void transfer(Administrator admin, Customer fromCustomer, Account fromAccount, Customer toCustomer, Account toAccount, double amount) {
		try {
			transfer(fromAccount, toAccount, amount);
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s successfully transfered $%.2f from user %s account %s to user %s account %s",
					admin.getUserName(), amount, fromCustomer.getUserName(), fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("TRANSFER: Administrator %s unsuccessfully tried to transfered $%.2f from user %s account %s to user %s account %s (%s)",
					admin.getUserName(), amount, fromCustomer.getUserName(), fromAccount.getNickName(), toCustomer.getUserName(), toAccount.getNickName(), e.getMessage()));
		}
	}

	/**
	 * Finds a customer based on a userName
	 * @param userName
	 * @return
	 * @throws NullPointerException
	 * @throws SQLException 
	 */
	private Customer findCustomer(String userName) throws NullPointerException, SQLException {
		if (userName == null)
			throw new NullPointerException();

		return customerDao.select(userName);
	}


	/**
	 * Allows an admin to cancel an account
	 * @param admin
	 * @param customerUserName
	 * @param accountIdx
	 * @return
	 */
	public void cancelAccount(Administrator admin, Customer customer, Account account) throws SQLException {
		if (admin == null || customer == null || account == null)
			throw new NullPointerException();

		try {
			accountDao.toggleActive(account.getId());
			LoggingUtil.logDebug(String.format("CANCEL_ACCOUNT: Adminsitrator %s successfully canceled user %s account %s",
					admin.getUserName(), customer.getUserName(), account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("CANCEL_ACCOUNT: Adminsitrator %s unsuccessfully tried to cancel user %s account %s (%s)",
					admin.getUserName(), customer.getUserName(), account.getNickName(), e.getMessage()));
		}
	}

	/**
	 * Allows an employee to get customer information
	 * @param employee
	 * @param customerUserName
	 * @return
	 */
	public String getCustomerInformation(Employee employee, String customerUserName) throws SQLException {
		Customer customer = findCustomer(customerUserName);

		if (customer == null)
			return null;

		return customer.toString();

	}

	/**
	 * Allows an employee to approve an account
	 * @param employee
	 * @param customerUserName
	 * @return
	 */
	public void approveAccount(Employee employee, Customer customer, Account account) throws SQLException {
		if (employee == null || customer == null || account == null)
			throw new NullPointerException();

		try {
			accountDao.approveAccount(account.getId());
			LoggingUtil.logDebug(String.format("APPROVE_ACCOUNT: Employee %s successfully approved user %s account %s",
					employee.getUserName(), customer.getUserName(), account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logDebug(String.format("APPROVE_ACCOUNT: Employee %s unsuccessfully tried to approve user %s account %s (%s)",
					employee.getUserName(), customer.getUserName(), account.getNickName(), e.getMessage()));
		}
	}

	/**
	 * Allows an employee to reject an account
	 * @param employee
	 * @param customerUserName
	 * @return
	 */
	public void rejectAccount(Employee employee, Customer customer, Account account) throws SQLException {
		if (employee == null || customer == null || account == null)
			throw new NullPointerException();

		try {
			accountDao.toggleActive(account.getId());
			LoggingUtil.logDebug(String.format("REJECT_ACCOUNT: Employee %s successfully rejected user %s account %s",
					employee.getUserName(), customer.getUserName(), account.getNickName()));
		} catch (SQLException e) {
			LoggingUtil.logError(String.format("REJECT_ACCOUNT: Employee %s unsuccessfully tried to reject user %s account %s (%s)",
					employee.getUserName(), customer.getUserName(), account.getNickName(), e.getMessage()));
		}
	}
}
