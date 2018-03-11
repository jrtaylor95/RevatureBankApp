package com.revature.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bank.users.Customer;

public class CustomerDao implements BankDao<Customer> {

	private Connection connection;
	
	public CustomerDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void create(Customer customer) throws SQLException {
		String sql = "INSERT INTO CUSTOMER (CUSTOMER_USER_NAME, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_PASSWORD)"
				+ "VALUES (?,?,?,?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, customer.getUserName());
		statement.setString(2, customer.getFirstName());
		statement.setString(3, customer.getLastName());
		statement.setString(4, customer.getPassword());
		
		statement.executeUpdate();
	}

	@Override
	public Customer select(int id) throws SQLException {
		String sql = "SELECT CUSTOMER_ID, CUSTOMER_USER_NAME, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_PASSWORD FROM CUSTOMER WHERE CUSTOMER_ID = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Customer.parseCustomer(set);
		else
			return null;
	}
	
	public Customer select(String userName) throws SQLException {
		String sql = "SELECT CUSTOMER_ID, CUSTOMER_USER_NAME, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_PASSWORD FROM CUSTOMER WHERE CUSTOMER_USER_NAME = ?";
		
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
		String sql = "SELECT * FROM CUSTOMER";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet set = statement.executeQuery();
		
		while (set.next()) {
			customers.add(Customer.parseCustomer(set));
		}
		
		return customers;
	}

	@Override
	public void update(Customer customer) throws SQLException {
		String sql = "UPDATE CUSTOMER SET CUSTOMER_USER_NAME = ?, CUSTOMER_FIRST_NAME = ?, CUSTOMER_LAST_NAME = ?, CUSTOMER_PASSWORD = ? WHERE CUSTOMER_ID = ?";
		
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
		String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}
	
	public void delete(String userName) throws SQLException {
		String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, userName);
		
		statement.executeUpdate();
	}

}
