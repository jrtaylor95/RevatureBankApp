package com.revature.bank.core;

import java.util.Scanner;

import com.revature.bank.users.User;

public class BankDriver {
	
	private static Scanner scanner;
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		
		User currentUser = null;
		while (true) {
			if (currentUser == null)
				currentUser = askLogon();
			else {
				
			}
		}
	}
	
	private static User askLogon() {
		System.out.println("Choose an option:\n"
				+ "\t1. Log In\n"
				+ "\t2. Register\n");
		int option = scanner.nextInt();
		
		switch (option) {
		case 1:
			return logon();
		case 2:
			return register();
		}
		
		return null;
	}
	
	private static User logon() {
		System.out.print("User Name: ");
		String userName = scanner.next();
		System.out.println("Password: ");
		String password = scanner.next();
		
		return null;
	}
	
	private static User register() {
		System.out.print("User Name: ");
		String userName = scanner.next();
		System.out.println("Password: ");
		String password = scanner.next();
		
		return null;
	}

}
