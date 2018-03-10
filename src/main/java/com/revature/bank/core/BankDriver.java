package com.revature.bank.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
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
		
		bank = init();
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
	}
	
	private static void viewMyInformation() {
		System.out.println(((Customer) currentUser).toString());
	}
	
	private static void viewCustomerInformation() {
		System.out.println("User: ");
		String customerUserName = scanner.next();
		System.out.println(bank.getCustomerInformation((Employee) currentUser, customerUserName));
	}
	
	private static void viewCustomers() {
		Collection<Customer> customers = bank.getCustomers();
		
		System.out.println("Customers");
		for (Customer customer : customers) {
			System.out.printf("Username: %s Name: %s %s", customer.getUserName(), customer.getFirstName(), customer.getLastName());
		}
	}
	
	private static void openAccount() {
		System.out.print("Account name: ");
		String accountName = scanner.next();
		
		bank.apply((Customer) currentUser, accountName);
	}
	
	private static void approveAccount() {
		System.out.print("From user: ");
		String user = scanner.next();
		
		bank.approveAccount((Employee) currentUser, user); 
	}
	
	private static void rejectAccount() {
		System.out.print("From user: ");
		String user = scanner.next();
		
		bank.rejectAccount((Employee) currentUser, user);
	}
	
	private static void cancelAccount() {
		System.out.print("From user: ");
		String user = scanner.next();
		
		int idx = 0;
		for (Account account: ((Customer) currentUser).getAccounts()) {
			System.out.printf("%d." + account.toString(), ++idx);
		}
		int accountIdx = scanner.nextInt();
		
		bank.cancelAccount((Administrator) currentUser, user, accountIdx);
	}
	
	private static void withdraw() {
		System.out.print("From account: \n");
		int idx = 0;
		for (Account account: ((Customer) currentUser).getAccounts()) {
			System.out.printf("%d." + account.toString(), ++idx);
		}
		int option = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.withdraw((Customer) currentUser, option, amount);
	}
	
	private static void withdrawAdmin() {
		System.out.println("From user: ");
		String user = scanner.next();
		System.out.print("From account: ");
		int option = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.withdraw((Administrator) currentUser, user, option, amount);
	}
	
	private static void deposit() {
		System.out.print("From account: \n");
		int idx = 0;
		for (Account account: ((Customer) currentUser).getAccounts()) {
			System.out.printf("%d." + account.toString(), ++idx);
		}
		int option = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.deposit((Customer) currentUser, option, amount);
	}
	
	private static void depositAdmin() {
		System.out.println("From user: ");
		String user = scanner.next();
		System.out.print("From account: ");
		int option = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.deposit((Administrator) currentUser, user, option, amount);
	}
	
	private static void transferBetweenAccounts() {
		int idx = 0;
		for (Account account: ((Customer) currentUser).getAccounts()) {
			System.out.printf("%d." + account.toString(), ++idx);
		}
		System.out.print("From account: ");
		int fromAccount = scanner.nextInt() - 1;
		System.out.print("To account: ");
		int toAccount = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.transfer((Customer) currentUser, fromAccount, toAccount, amount);
	}
	
	private static void transferBetweenAccountsAdmin() {
		System.out.println("From user: ");
		String user = scanner.next();
		System.out.print("From account: ");
		int fromAccount = scanner.nextInt() - 1;
		System.out.print("To account: ");
		int toAccount = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.transfer((Administrator) currentUser, user, fromAccount, toAccount, amount);
	}
	
	private static void transferBetweenUsers() {
		int idx = 0;
		for (Account account: ((Customer) currentUser).getAccounts()) {
			System.out.printf("%d." + account.toString(), ++idx);
		}
		System.out.print("From account: ");
		int fromAccount = scanner.nextInt() - 1;
		System.out.print("To user: ");
		String toUser = scanner.next();
		System.out.print("To account: ");
		int toAccount = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.transfer((Customer) currentUser, fromAccount, toUser, toAccount, amount);
	}
	
	private static void transferBetweenUsersAdmin() {
		System.out.println("From user: ");
		String user = scanner.next();
		System.out.print("From account: ");
		int fromAccount = scanner.nextInt() - 1;
		System.out.print("To user: ");
		String toUser = scanner.next();
		System.out.print("To account: ");
		int toAccount = scanner.nextInt() - 1;
		System.out.println("Amount: ");
		double amount = scanner.nextDouble();
		bank.transfer((Administrator) currentUser, user, fromAccount, toUser, toAccount, amount);
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
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("bank.dat"))) {
			stream.writeObject(bank);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = false;
	}
	
	private static User askLogon() {
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
		
		return bank.logon(userName, password);
	}
	
	private static User register(boolean isEmployee) {
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
