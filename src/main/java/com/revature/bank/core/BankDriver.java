package com.revature.bank.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Administrator;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;
import com.revature.bank.users.User;

public class BankDriver {

	private static Scanner scanner;
	private static Bank bank;
	private static User currentUser;
	private static boolean run = true;


	public static void main(String[] args) {
		scanner = new Scanner(System.in);

		bank = new Bank();

		try {
			while (run) {
				if (currentUser == null)
					currentUser = askLogon();
				else {
					int option = accountOptions(currentUser);
					switch (option) {
					case 1:
						viewMyInformation();
						break;
					case 2:
						withdraw();
						break;
					case 3:
						deposit();
						break;
					case 4:
						transferBetweenAccounts();
						break;
					case 5:
						transferBetweenUsers();
						break;
					case 6:
						openAccount();
						break;
					case 7:
						exit();
						break;
					case 8:
						viewCustomers();
						break;
					case 9:
						viewCustomerInformation();
						break;
					case 10:
						approveAccount();
						break;
					case 11:
						rejectAccount();
						break;
					case 12:
						cancelAccount();
						break;
					case 13:
						withdrawAdmin();
						break;
					case 14:
						depositAdmin();
						break;
					case 15:
						transferBetweenAccountsAdmin();
						break;
					case 16:
						transferBetweenUsersAdmin();
						break;
					case 17:
						exit();
						break;
					case 18:
						viewCustomers();
						break;
					case 19:
						viewCustomerInformation();
						break;
					case 20:
						approveAccount();
						break;
					case 21:
						rejectAccount();
						break;
					case 22:
						exit();
						break;
					}
				}
			}
		} catch (SQLException e) {
			System.out.printf("Something went wrong (%s)\n", e.getMessage());
		}
	}

	private static void viewMyInformation() {
		System.out.println(((Customer) currentUser).toString());
	}

	private static void viewCustomerInformation() throws SQLException {
		System.out.println("User: ");
		String customerUserName = scanner.next();
		String info = bank.getCustomerInformation((Employee) currentUser, customerUserName);

		if (info != null)
			System.out.println(info);
		else {
			System.out.printf("User %s does not exist!%n", customerUserName);
			viewCustomerInformation();
		}
	}

	private static void viewCustomers() {
		Collection<Customer> customers = null;
		try {
			customers = bank.getCustomers();
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}

		if (customers != null) {
			System.out.println("Customers");
			for (Customer customer : customers) {
				System.out.printf("Username: %s Name: %s %s\n", customer.getUserName(), customer.getFirstName(), customer.getLastName());
			}
		}
	}

	private static void openAccount() {
		System.out.print("Account name: ");
		String accountName = scanner.next();

		bank.apply((Customer) currentUser, accountName);
	}

	private static void approveAccount() {
		System.out.print("From user: ");
		String userName = scanner.next();

		Customer user;
		try {
			user = bank.getCustomer(userName);
			List<Account> accounts = bank.getPendingAccounts(user);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", user.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			bank.approveAccount((Employee) currentUser, user, accounts.get(accountIdx)); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void rejectAccount() throws SQLException {
		System.out.print("From user: ");
		String userName = scanner.next();

		Customer user;
		try {
			user = bank.getCustomer(userName);
			List<Account> accounts = bank.getPendingAccounts(user);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", user.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			bank.rejectAccount((Employee) currentUser, user, accounts.get(accountIdx)); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void cancelAccount() throws SQLException {
		System.out.print("From user: ");
		String userName = scanner.next();

		Customer user;
		try {
			user = bank.getCustomer(userName);
			List<Account> accounts = bank.getAccounts(user);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", user.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			bank.cancelAccount((Administrator) currentUser, user, accounts.get(accountIdx)); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void withdraw() {
		Customer customer = (Customer) currentUser;
		System.out.print("From account: \n");
		try {
			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.println("You have no active accounts\n");
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.withdraw(customer, accounts.get(accountIdx), amount); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void withdrawAdmin() throws SQLException {
		Administrator admin = (Administrator) currentUser;

		System.out.print("From customer: ");
		String customerUserName = scanner.next();

		try {
			Customer customer = bank.getCustomer(customerUserName);

			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", currentUser.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.withdraw(admin, customer, accounts.get(accountIdx), amount); 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void deposit() {
		System.out.print("From account: \n");
		Customer customer = (Customer) currentUser;
		try {
			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.println("You have no active accounts\n");
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.deposit(customer, accounts.get(accountIdx), amount); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void depositAdmin() throws SQLException {
		Administrator admin = (Administrator) currentUser;

		System.out.print("From customer: ");
		String customerUserName = scanner.next();

		try {
			Customer customer = bank.getCustomer(customerUserName);

			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", customer.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("Choose Account: ");
			int accountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.deposit(admin, customer, accounts.get(accountIdx), amount); 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void transferBetweenAccounts() {
		System.out.print("From account: \n");
		Customer customer = (Customer) currentUser;
		try {
			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.println("You have no active accounts\n");
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("From Account: ");
			int fromAccountIdx = scanner.nextInt();
			System.out.print("To Account: ");
			int toAccountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.transfer(customer, accounts.get(fromAccountIdx), accounts.get(toAccountIdx), amount); 
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please contact an admin.");
		}
	}

	private static void transferBetweenAccountsAdmin() throws SQLException {
		Administrator admin = (Administrator) currentUser;

		System.out.print("From customer: ");
		String customerUserName = scanner.next();

		try {
			Customer customer = bank.getCustomer(customerUserName);

			List<Account> accounts = bank.getAccounts(customer);
			
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", customer.getUserName());
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("From Account: ");
			int fromAccountIdx = scanner.nextInt();
			System.out.print("To Account: ");
			int toAccountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.transfer(admin, customer, accounts.get(fromAccountIdx), accounts.get(toAccountIdx), amount);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void transferBetweenUsers() throws SQLException {
		Customer fromCustomer = (Customer) currentUser;

		try {
			List<Account> accounts = bank.getAccounts(fromCustomer);
			if (accounts.isEmpty()) {
				System.out.println("You have no accounts");
				return;
			}
			
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("From Account: ");
			int fromAccountIdx = scanner.nextInt();
			
			System.out.print("To customer: ");
			String toCustomerUserName = scanner.next();
			Customer toCustomer = bank.getCustomer(toCustomerUserName);
			List<Account> toAccounts = bank.getAccounts(toCustomer);
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", toCustomer.getUserName());
				return;
			}
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("To Account: ");
			int toAccountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.transfer(fromCustomer, accounts.get(fromAccountIdx), toCustomer, toAccounts.get(toAccountIdx), amount);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void transferBetweenUsersAdmin() throws SQLException {
		Administrator admin = (Administrator) currentUser;

		System.out.print("From customer: ");
		String fromCustomerUserName = scanner.next();

		try {
			Customer fromCustomer = bank.getCustomer(fromCustomerUserName);

			List<Account> accounts = bank.getAccounts(fromCustomer);
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", fromCustomer.getUserName());
				return;
			}
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("From Account: ");
			int fromAccountIdx = scanner.nextInt();
			
			System.out.print("To customer: ");
			String toCustomerUserName = scanner.next();
			Customer toCustomer = bank.getCustomer(toCustomerUserName);
			List<Account> toAccounts = bank.getAccounts(toCustomer);
			if (accounts.isEmpty()) {
				System.out.printf("User %s has no pending accounts\n", toCustomer.getUserName());
				return;
			}
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%d. %s\n", i, accounts.get(i).getNickName());
			}

			System.out.print("To Account: ");
			int toAccountIdx = scanner.nextInt();
			System.out.print("Amount: $");
			double amount = scanner.nextDouble();
			bank.transfer(admin, fromCustomer, accounts.get(fromAccountIdx), toCustomer, toAccounts.get(toAccountIdx), amount);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static Bank init() {
		Bank bank = null;

		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("bank.dat"))) {
			bank = (Bank) stream.readObject();
		} catch (FileNotFoundException e) {
			bank = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bank == null)
			return new Bank();
		else
			return bank;
	}

	private static void exit() {
//		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("bank.dat"))) {
//			stream.writeObject(bank);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		run = false;
	}

	private static User askLogon() throws SQLException {
		System.out.println("Choose an option:\n"
				+ "\t1. Log In\n"
				+ "\t2. Register\n"
				+ "\t3. Register Employee\n");
		int option = scanner.nextInt();

		switch (option) {
		case 1:
			return logon();
		case 2:
			return register(false);
		case 3:
			return register(true);
		}

		return null;
	}

	private static User logon() {
		System.out.print("User Name: ");
		String userName = scanner.next();
		System.out.println("Password: ");
		String password = scanner.next();

		User user = null;
		
		try {
			user = bank.logon(userName, password);
		} catch (SQLException e) {
			System.out.println("Something went wrong!");
		}
		
		return user;
	}

	private static User register(boolean isEmployee) throws SQLException {
		System.out.print("User Name: ");
		String userName = scanner.next();
		System.out.print("Password: ");
		String password = scanner.next();
		System.out.print("First Name: ");
		String firstName = scanner.next();
		System.out.print("Last Name: ");
		String lastName = scanner.next();
		boolean isAdmin = false;

		if (isEmployee) {
			System.out.print("Administrator (true/false): " );
			isAdmin = scanner.nextBoolean();
			return bank.registerEmployee(userName, password, isAdmin, firstName, lastName);
		} else {
			return bank.register(userName, password, firstName, lastName);
		}
	}

	private static int accountOptions(User user) {
		int offset = 0;
		if (user instanceof Customer) {
			System.out.println("Choose an option:\n"
					+ "\t1. View accounts\n"
					+ "\t2. Withdraw\n"
					+ "\t3. Deposit\n"
					+ "\t4. Transfer to another account\n"
					+ "\t5. Transfer to another user\n"
					+ "\t6. Open new account\n"
					+ "\t7. Log out\n");
		} else if(user instanceof Administrator) {
			System.out.println("Choose an option:\n"
					+ "\t1. View users\n"
					+ "\t2. View specific user\n"
					+ "\t3. Approve account\n"
					+ "\t4. Deny account\n"
					+ "\t5. Cancel account\n"
					+ "\t6. Withdraw\n"
					+ "\t7. Deposit\n"
					+ "\t8. Transfer between accounts\n"
					+ "\t9. Transfer between users\n"
					+ "\t10. Log out\n");
			offset = 7;
		} else if (user instanceof Employee) {
			System.out.println("Choose an option:\n"
					+ "\t1. View users\n"
					+ "\t2. View specific user\n"
					+ "\t3. Approve account\n"
					+ "\t4. Deny account\n"
					+ "\t5. Log out\n");
			offset = 17;
		}
		int option = scanner.nextInt();

		return option + offset;
	}

}
