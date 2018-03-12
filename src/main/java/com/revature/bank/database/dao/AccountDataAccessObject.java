package com.revature.bank.database.dao;

import static com.revature.bank.database.AccountTable.*;
import static com.revature.bank.database.CustomerTable.C_USER_NAME;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Customer;

public class AccountDataAccessObject implements DataAccessObject<Account> {

	private Connection connection;
	
	public AccountDataAccessObject(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void create(Account account) throws SQLException {
		String sql = String.format("INSERT INTO ACCOUNT (%s) VALUES (?)",
				A_NICKNAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, account.getNickName());
		
		statement.executeUpdate();
	}
	
	public void create(Account account, Customer customer) throws SQLException {
		String sql = "{call CREATE_ACCOUNT(?, ?)}";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, account.getNickName());
		statement.setString(2, customer.getUserName());
		statement.executeUpdate();
	}

	@Override
	public Account select(int id) throws SQLException {
		String sql = String.format("SELECT %s, %s, %s FROM ACTIVE_ACCOUNT WHERE %S = ?",
				A_ID, A_NICKNAME, A_BALANCE, A_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet set = statement.executeQuery();
		if (set.next())
			return Account.parseAccount(set);
		else
			return null;
	}
	
	public List<Account> select(String customerUserName) throws SQLException {
		List<Account> accounts = new ArrayList<>();
		String sql = String.format("SELECT %s, %s, %s FROM CUSTOMER_ACCOUNT_VIEW WHERE %s = ?",
				A_ID, A_NICKNAME, A_BALANCE, C_USER_NAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, customerUserName);
		
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			accounts.add(Account.parseAccount(set));
		}
		
		return accounts;
	}
	
	public List<Account> selectPending(String customerUserName) throws SQLException {
		List<Account> accounts = new ArrayList<>();
		String sql = String.format("SELECT %s, %s, %s FROM PENDING_ACCOUNT WHERE %s = ?",
				A_ID, A_NICKNAME, A_BALANCE, C_USER_NAME);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, customerUserName);
		
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			accounts.add(Account.parseAccount(set));
		}
		
		return accounts;
	}

	@Override
	public List<Account> selectAll() throws SQLException {
		List<Account> accounts = new ArrayList<>();
		String sql = String.format("SELECT %s, %s, %s FROM ACTIVE_ACCOUNT",
				A_ID, A_NICKNAME, A_BALANCE);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			accounts.add(Account.parseAccount(set));
		}
		
		return accounts;
	}

	@Override
	public void update(Account account) throws SQLException {
		String sql = String.format("UPDATE ACCOUNT SET %s = ?, %s = ? WHERE %s = ?",
				A_NICKNAME, A_BALANCE, A_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, account.getNickName());
		statement.setDouble(2, account.getBalance());
		statement.setInt(3, account.getId());
		
		statement.executeUpdate();
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = String.format("DELETE FROM ACCOUNT WHERE %s = ?",
				A_ID);
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}
	
	public void toggleActive(int id) throws SQLException {
		String sql = "{call TOGGLE_ACCOUNT_ACTIVE(?)}";
		
		CallableStatement statement = connection.prepareCall(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}
	
	public void approveAccount(int id) throws SQLException {
		String sql = "{call APPROVE_ACCOUNT(?)}";
		
		CallableStatement statement = connection.prepareCall(sql);
		statement.setInt(1, id);
		
		statement.executeUpdate();
	}

}
