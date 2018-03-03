package com.revature.bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.bank.accounts.Account;
import com.revature.bank.users.Customer;

public class CustomerTests {

	@Rule
	ExpectedException expectedException = ExpectedException.none();
	
	Customer customer;
	
	@Before
	public void setUp() throws Exception {
		customer = new Customer("Customer", "Password");
	}

	@Test
	public void testAddAccount() {
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(expectedAccount, actualAccount);
	}
	
	@Test
	public void testWithdaw() {
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
		assertTrue(customer.withdraw(0, 250));
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testDeposit() {
		Account expectedAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		assertTrue(customer.deposit(0, 500));
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(500, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testTransfer() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		Account expectedAccount = new Account("Checking");
		Account expectedTransferAccount = new Account("Checking");
		expectedAccount.deposit(500);
		expectedTransferAccount.deposit(500);
		customer.addAccount(expectedAccount);
		transferCustomer.addAccount(expectedTransferAccount);
		
		assertTrue(customer.transfer(0, transferCustomer, 0, 250));
		Account actualAccount = customer.getAccount(0);
		Account actualTransferAccount = transferCustomer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
		assertEquals(750, actualTransferAccount.getBalance(), 0);
	}
	
	@Test
	public void testNegativeWithdraw() {
		Account expectedAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		customer.withdraw(0, -100);
	}
	
	@Test
	public void testNegativeDeposit() {
		Account expectedAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		customer.deposit(0, -100);
	}
	
	@Test
	public void testNegativeTransfer() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		Account expectedAccount = new Account("Checking");
		Account expectedTransferAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		customer.transfer(0, transferCustomer, 0, -100);
	}
	
	@Test
	public void testOverWithdraw() {
		Account expectedAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		
		expectedException.expect(IllegalStateException.class);
		customer.withdraw(0, 500);
	}
	
	@Test
	public void testOverTransfer() {
		Customer transfer = new Customer("Transfer", "Password");
		Account expectedAccount = new Account("Checking");
		Account expectedTransferAccount = new Account("Checking");
		customer.addAccount(expectedAccount);
		transfer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalStateException.class);
		customer.transfer(0, transfer, 0, 500);
	}
	
}
