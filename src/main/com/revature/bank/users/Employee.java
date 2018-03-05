package com.revature.bank.users;

public class Employee extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2468531644845540457L;

	public Employee(String userName, String password) {
		super(userName, password);
	}
	
	public String toString() {
		return String.format("Employee %s\n"
				+ "Name: %s %s\n"
				+ "Role: Employee", this.getUserName(), this.getFirstName(), this.getLastName());
	}
}
