package com.revature.bank.users;

public class Administrator extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4607580623653880109L;

	public Administrator(String userName, String password) {
		super(userName, password);
	}
	
	public String toString() {
		return String.format("Employee %s\n"
				+ "Name: %s %s\n"
				+ "Role: Administrator", this.getUserName(), this.getFirstName(), this.getLastName());
	}
}
