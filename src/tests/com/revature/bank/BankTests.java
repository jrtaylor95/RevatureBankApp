package com.revature.bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.bank.accounts.Account;
import com.revature.bank.core.Bank;
import com.revature.bank.users.Administrator;
import com.revature.bank.users.Customer;

public class BankTests {

	Bank bank;
	private Customer customer;
	private Administrator admin;
	private ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		customer = (Customer) bank.register("Customer", "Password");
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
		admin = new Administrator("Admin", "Password");
	}
	
	@Test
	public void testRegisterLogon() {
		Customer expectedCustomer = (Customer) bank.register("New Customer", "Password");
		assertEquals(2, bank.getCustomers().size());
		Customer actualCustomer = (Customer) bank.logon("New Customer", "Password");
		assertEquals(expectedCustomer, actualCustomer);
	}
	
	@Test
	public void testWithdaw() {
		assertTrue(bank.withdraw(customer, 0, 500));
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testDeposit() {
		assertTrue(bank.deposit(customer, 0, 500));
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(1000, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testTransfer() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		expectedTransferAccount.deposit(500);
		transferCustomer.addAccount(expectedTransferAccount);
		
		assertTrue(bank.transfer(customer, 0, "Transfer", 0, 250));
		Account actualAccount = customer.getAccount(0);
		Account actualTransferAccount = transferCustomer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
		assertEquals(750, actualTransferAccount.getBalance(), 0);
	}
	
	@Test
	public void testNegativeWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		bank.withdraw(customer, 0, -100);
	}
	
	@Test
	public void testNegativeDeposit() {
		expectedException.expect(IllegalArgumentException.class);
		bank.deposit(customer, 0, -100);
	}
	
	@Test
	public void testNegativeTransfer() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		bank.transfer(customer, 0, "Tansfer", 0, -100);
	}
	
	@Test
	public void testOverWithdraw() {
		expectedException.expect(IllegalStateException.class);
		bank.withdraw(customer, 0, 750);
	}
	
	@Test
	public void testOverTransfer() {
		Customer transfer = (Customer) bank.register("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transfer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalStateException.class);
		bank.transfer(customer, 0, "Transfer", 0, 750);
	}
	
	@Test
	public void testTransferToNull() {
		expectedException.expect(NullPointerException.class);
		bank.transfer(customer, 0, null, 0, 500);
	}
	
	@Test 
	public void testTransferToNoAccount() {
		assertNotNull(bank.register("Transfer", "Password"));
		expectedException.expect(IllegalStateException.class);
		bank.transfer(customer, 0, "Transfer", 0, 500);
	}
	
	@Test
	public void testTransferOutOfBounds() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(ArrayIndexOutOfBoundsException.class);
		bank.transfer(customer, 5, "Transfer", 0, 500);
		expectedException.expect(ArrayIndexOutOfBoundsException.class);
		bank.transfer(customer, 0, "Transfer", 5, 500);
	}
	
	@Test
	public void testAdminWithdraw() {
		assertTrue(bank.withdraw(admin, "Customer", 0, 250));
		Account actualAccount = customer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testAdminDeposit() {
		assertTrue(bank.deposit(admin, "Customer", 0, 250));
		Account actualAccount = customer.getAccount(0);
		assertEquals(750, actualAccount.getBalance(), 0);
	}
	
	@Test
	public void testAdminTransferSameCustomer() {
		Account transferAccount = new Account("Checking - 2");
		customer.addAccount(transferAccount);
		
		assertTrue(bank.transfer(admin, "Customer", 0, 1, 250));
		Account expectedAccount = customer.getAccount(0);
		assertEquals(250, expectedAccount.getBalance(), 0);
		assertEquals(250, transferAccount.getBalance(), 0);
	}
	
	@Test
	public void testAdminTransferDifferentCustomer() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password");
		Account transferAccount = new Account("Checking");
		transferCustomer.addAccount(transferAccount);
		
		assertTrue(bank.transfer(admin, "Customer", 0, "Transfer", 0, 250));
		Account expectedAccount = customer.getAccount(0);
		assertEquals(250, expectedAccount.getBalance(), 0);
		assertEquals(250, transferAccount.getBalance(), 0);
	}

}
