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
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
	}

	@Test
	public void testAddAccount() {
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(expectedAccount, actualAccount);
	}
	
	@Test
	public void testWithdaw() {
		assertTrue(customer.withdraw(0, 250));
		Account actualAccount = customer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testDeposit() {
		assertTrue(customer.deposit(0, 500));
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(1000, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testTransfer() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		expectedTransferAccount.deposit(500);
		transferCustomer.addAccount(expectedTransferAccount);
		
		assertTrue(customer.transfer(0, transferCustomer, 0, 250));
		Account actualAccount = customer.getAccount(0);
		Account actualTransferAccount = transferCustomer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
		assertEquals(750, actualTransferAccount.getBalance(), 0);
	}
	
	@Test
	public void testNegativeWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		customer.withdraw(0, -100);
	}
	
	@Test
	public void testNegativeDeposit() {
		expectedException.expect(IllegalArgumentException.class);
		customer.deposit(0, -100);
	}
	
	@Test
	public void testNegativeTransfer() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		customer.transfer(0, transferCustomer, 0, -100);
	}
	
	@Test
	public void testOverWithdraw() {
		expectedException.expect(IllegalStateException.class);
		customer.withdraw(0, 750);
	}
	
	@Test
	public void testOverTransfer() {
		Customer transfer = new Customer("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transfer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalStateException.class);
		customer.transfer(0, transfer, 0, 750);
	}
	
	@Test
	public void testTransferToNull() {
		expectedException.expect(NullPointerException.class);
		customer.transfer(0, null, 0, 500);
	}
	
	@Test 
	public void testTransferToNoAccount() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		
		expectedException.expect(IllegalStateException.class);
		customer.transfer(0, transferCustomer, 0, 500);
	}
	
	@Test
	public void testTransferOutOfBounds() {
		Customer transferCustomer = new Customer("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(ArrayIndexOutOfBoundsException.class);
		customer.transfer(5, transferCustomer, 0, 500);
		expectedException.expect(ArrayIndexOutOfBoundsException.class);
		customer.transfer(0, transferCustomer, 5, 500);
	}
	
}
