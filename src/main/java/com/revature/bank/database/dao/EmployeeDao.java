package com.revature.bank.database.dao;

import static com.revature.bank.database.EmployeeTable.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bank.users.Employee;

public class EmployeeDao implements BankDao<Employee> {

	private Connection connection;
	
	public EmployeeDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void create(Employee employee) throws SQLException {
		String sql = String.format("INSERT INTO EMPLOYEE (%s, %s, %s, %s) VALUES (?,?,?,?)",
				EMPLOYEE_USER_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_PASSWORD);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, employee.getUserName());
		statement.setString(2, employee.getFirstName());
		statement.setString(3, employee.getLastName());
		statement.setString(4, employee.getPassword());
		
		statement.executeUpdate();
	}

	@Override
	public Employee select(int id) throws SQLException {
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM EMPLOYEE WHERE %s = ?",
				EMPLOYEE_ID, EMPLOYEE_USER_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_PASSWORD, EMPLOYEE_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Employee.parseCustomer(set);
		else
			return null;
	}
	
	public Employee select(String userName) throws SQLException {
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM EMPLOYEE WHERE %s = ?",
				EMPLOYEE_ID, EMPLOYEE_USER_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_PASSWORD, EMPLOYEE_USER_NAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Employee.parseCustomer(set);
		else
			return null;
	}

	@Override
	public List<Employee> selectAll() throws SQLException {
		List<Employee> employees = new ArrayList<>();
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM EMPLOYEE",
				EMPLOYEE_ID, EMPLOYEE_USER_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_PASSWORD);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet set = statement.executeQuery();
		
		while (set.next()) {
			employees.add(Employee.parseCustomer(set));
		}
		
		return employees;
	}

	@Override
	public void update(Employee employee) throws SQLException {
		String sql = String.format("UPDATE EMPLOYEE SET %S = ?, %S = ?, %S = ?, %S = ? WHERE %S = ?",
				EMPLOYEE_USER_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_PASSWORD);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, employee.getUserName());
		statement.setString(2, employee.getFirstName());
		statement.setString(3, employee.getLastName());
		statement.setString(4, employee.getPassword());
		statement.setInt(5, employee.getID());
		
		statement.executeUpdate();
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = String.format("DELETE FROM EMPLOYEE WHERE %s = ?",
				EMPLOYEE_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}
	
	public void delete(String userName) throws SQLException {
		String sql = String.format("DELETE FROM EMPLOYEE WHERE %s = ?",
				EMPLOYEE_USER_NAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		
		statement.executeUpdate();
	}

}
