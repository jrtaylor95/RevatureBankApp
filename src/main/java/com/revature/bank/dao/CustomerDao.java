package com.revature.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
	public Customer select(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> selectAll() {
		// TODO Auto-generated method stub
		return null;
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
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

}
