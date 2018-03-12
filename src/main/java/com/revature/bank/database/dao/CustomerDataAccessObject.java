package com.revature.bank.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.revature.bank.database.CustomerTable.*;
import com.revature.bank.users.Customer;

public class CustomerDataAccessObject implements DataAccessObject<Customer> {

	private Connection connection;
	
	public CustomerDataAccessObject(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void create(Customer customer) throws SQLException {
		String sql = String.format("INSERT INTO CUSTOMER (%s, %s, %s, %s) VALUES (?,?,?,?)",
				C_USER_NAME, C_FIRST_NAME, C_LAST_NAME, C_PASSWORD);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, customer.getUserName());
		statement.setString(2, customer.getFirstName());
		statement.setString(3, customer.getLastName());
		statement.setString(4, customer.getPassword());
		
		statement.executeUpdate();
	}

	@Override
	public Customer select(int id) throws SQLException {
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM CUSTOMER WHERE %s = ?",
				C_ID, C_USER_NAME, C_FIRST_NAME, C_LAST_NAME, C_PASSWORD, C_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Customer.parseCustomer(set);
		else
			return null;
	}
	
	public Customer select(String userName) throws SQLException {
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM CUSTOMER WHERE %s = ?",
				C_ID, C_USER_NAME, C_FIRST_NAME, C_LAST_NAME, C_PASSWORD, C_USER_NAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Customer.parseCustomer(set);
		else
			return null;
	}

	@Override
	public List<Customer> selectAll() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String sql = String.format("SELECT %s, %s, %s, %s, %s FROM ACTIVE_CUSTOMER",
				C_ID, C_USER_NAME, C_FIRST_NAME, C_LAST_NAME, C_PASSWORD);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet set = statement.executeQuery();
		
		while (set.next()) {
			customers.add(Customer.parseCustomer(set));
		}
		
		return customers;
	}

	@Override
	public void update(Customer customer) throws SQLException {
		String sql = String.format("UPDATE CUSTOMER SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
				C_USER_NAME, C_FIRST_NAME, C_LAST_NAME, C_PASSWORD, C_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, customer.getUserName());
		statement.setString(2, customer.getFirstName());
		statement.setString(3, customer.getLastName());
		statement.setString(4, customer.getPassword());
		statement.setInt(5, customer.getID());
		
		statement.executeUpdate();
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = String.format("DELETE FROM CUSTOMER WHERE %s = ?",
				C_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}
	
	public void delete(String userName) throws SQLException {
		String sql = String.format("DELETE FROM CUSTOMER WHERE %s = ?",
				C_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		
		statement.executeUpdate();
	}
	
	public void toggleActive(int id) throws SQLException {
		String sql = "{call TOGGLE_CUSTOMER_ACTIVE(?)}";
		
		CallableStatement statement = connection.prepareCall(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}

}
