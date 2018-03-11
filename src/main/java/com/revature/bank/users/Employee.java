package com.revature.bank.users;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2468531644845540457L;

	public static Employee parseCustomer(ResultSet set) throws SQLException {
		Employee employee = new Employee();

		employee.setID(set.getInt(1));
		employee.setUserName(set.getString(2));
		employee.setFirstName(set.getString(3));
		employee.setLastName(set.getString(4));
		employee.setPassword(set.getString(5));
		
		return employee;
	}
	
	public Employee() {
		
	}
	
	public Employee(String userName, String password) {
		super(userName, password);
	}
	
	public String toString() {
		return String.format("Employee %s\n"
				+ "Name: %s %s\n"
				+ "Role: Employee", this.getUserName(), this.getFirstName(), this.getLastName());
	}
}
