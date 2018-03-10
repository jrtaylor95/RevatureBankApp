package com.revature.bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.bank.accounts.Account;
import com.revature.bank.core.Bank;
import com.revature.bank.users.Administrator;
import com.revature.bank.users.Customer;
import com.revature.bank.users.Employee;

public class BankTests {

	Bank bank;
	private Customer customer;
	private Administrator admin;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		customer = (Customer) bank.register("Customer", "Password", "Michael", "Bluth");
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
		admin = (Administrator) bank.registerEmployee("Admin", "Password", true,  "Gob", "Bluth");
	}
	
	@Test
	public void testRegisterLogon() {
		Customer expectedCustomer = (Customer) bank.register("New Customer", "Password", "Michael", "Bluth");
		assertEquals(2, bank.getCustomers().size());
		Customer actualCustomer = (Customer) bank.logon("New Customer", "Password");
		assertEquals(expectedCustomer, actualCustomer);
	}
	
	@Test
	public void testLogonUserNameNull() {
		expectedException.expect(NullPointerException.class);
		bank.logon(null, "Password");
	}
	
	@Test
	public void testLogonUserPasswordNull() {
		expectedException.expect(NullPointerException.class);
		bank.logon("Customer", null);
	}
	
	@Test
	public void testEmployeeLogon() {
		Administrator expectedCustomer = (Administrator) bank.logon("Admin", "Password");
		assertNotNull(expectedCustomer);
	}
	
	@Test
	public void testLogonWrongPassword() {
		Customer expectedCustomer = (Customer) bank.logon("Customer", "Password1");
		assertNull(expectedCustomer);
	}
	
	@Test
	public void testInvalidLogon() {
		Administrator expectedCustomer = (Administrator) bank.logon("Admin1", "Password");
		assertNull(expectedCustomer);
	}
	
	@Test
	public void testDuplicateRegister() {
		Customer duplicateCustomer = (Customer) bank.register("Customer", "Password", "Michael", "Bluth");
		assertNull(duplicateCustomer);
		assertEquals(1, bank.getCustomers().size());
	}
	
	@Test
	public void testCustomerRegisterWithEmployee() {
		Customer expectedCustomer = (Customer) bank.register("Admin", "Password", "Gob", "Bluth");
		assertNull(expectedCustomer);
		assertEquals(1, bank.getCustomers().size());
	}
	
	@Test
	public void testCustomerRegisterWithNull() {
		expectedException.expect(NullPointerException.class);
		bank.register(null, "Password", "Michael", "Bluth");
	}
	
	@Test
	public void testRegisterEmployee() {
		Employee expectedCustomer = (Employee) bank.registerEmployee("Employee", "Password", true,  "Mae", "Funke");
		assertNotNull(expectedCustomer);
		assertEquals(2, bank.getEmployees().size());
	}
	
	@Test
	public void testRegisterDuplicateEmployee() {
		Employee expectedEmployee = (Administrator) bank.registerEmployee("Admin", "Password", true,  "Gob", "Bluth");
		assertNull(expectedEmployee);
		assertEquals(1, bank.getEmployees().size());
	}
	
	@Test
	public void testRegisterEmployeeWithCustomer() {
		Employee expectedEmployee = (Administrator) bank.registerEmployee("Customer", "Password", true, "Michael", "Bluth");
		assertNull(expectedEmployee);
		assertEquals(1, bank.getEmployees().size());
	}
	
	@Test
	public void testWithdaw() {
		assertTrue(bank.withdraw(customer, 0, 250));
		
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
	public void testIntraUserTransfer() {
		Account expectedTrasferAccount = new Account("Checking - 2");
		customer.addAccount(expectedTrasferAccount);
		
		assertTrue(bank.transfer(customer, 0, 1, 250));
		Account actualAccount = customer.getAccount(0);
		Account actualTransferAccount = customer.getAccount(1);
		assertEquals(250, actualAccount.getBalance(), 0);
		assertEquals(250, actualTransferAccount.getBalance(), 0);
	}
	
	@Test
	public void testTransfer() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
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
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		bank.transfer(customer, 0, "Transfer", 0, -100);
	}
	
	@Test
	public void testOverWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		bank.withdraw(customer, 0, 750);
	}
	
	@Test
	public void testOverTransfer() {
		Customer transfer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
		Account expectedTransferAccount = new Account("Checking");
		transfer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IllegalArgumentException.class);
		bank.transfer(customer, 0, "Transfer", 0, 750);
	}
	
	@Test
	public void testTransferToNull() {
		expectedException.expect(NullPointerException.class);
		bank.transfer(customer, 0, null, 0, 500);
	}
	
	@Test 
	public void testTransferToNoAccount() {
		assertNotNull(bank.register("Transfer", "Password", "Linday", "Bluth Funke"));
		expectedException.expect(IndexOutOfBoundsException.class);
		bank.transfer(customer, 0, "Transfer", 0, 500);
	}
	
	@Test
	public void testTransferFromOutOfBounds() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IndexOutOfBoundsException.class);
		bank.transfer(customer, 5, "Transfer", 0, 500);
	}
	
	@Test
	public void testTransferToOutOfBounds() {
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
		Account expectedTransferAccount = new Account("Checking");
		transferCustomer.addAccount(expectedTransferAccount);
		
		expectedException.expect(IndexOutOfBoundsException.class);
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
		Customer transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
		Account transferAccount = new Account("Checking");
		transferCustomer.addAccount(transferAccount);
		
		assertTrue(bank.transfer(admin, "Customer", 0, "Transfer", 0, 250));
		Account expectedAccount = customer.getAccount(0);
		assertEquals(250, expectedAccount.getBalance(), 0);
		assertEquals(250, transferAccount.getBalance(), 0);
	}
	
	@Test
	public void testApproveAccount() {
		bank.apply(customer, "Checking - 2");
		assertEquals(1, bank.getPendingAccounts().size());
		bank.approveAccount(admin, customer.getUserName());
		assertEquals(2, customer.getAccounts().size());
		assertEquals(0, bank.getPendingAccounts().size());
	}
	
	@Test
	public void testApproveAccountNull() {
		expectedException.expect(NullPointerException.class);
		bank.approveAccount(admin, null);
	}
	
	@Test
	public void testApproveAccountNoCustomer() {
		assertFalse(bank.approveAccount(admin, "Customer"));
	}
	
	@Test
	public void testRejectAccount() {
		bank.apply(customer, "Checking - 2");
		assertEquals(1, bank.getPendingAccounts().size());
		bank.rejectAccount(admin, customer.getUserName());
		assertEquals(1, customer.getAccounts().size());
		assertEquals(0, bank.getPendingAccounts().size());
	}
	
	@Test
	public void testCancelAccount() {
		assertTrue(bank.cancelAccount(admin, "Customer", 0));
		
		assertTrue(customer.getAccounts().isEmpty());
	}
	
	@Test
	public void testCancelAccountOutOfBounds() {
		expectedException.expect(IndexOutOfBoundsException.class);
		bank.cancelAccount(admin, "Customer", 1);
	}
	
	@Test
	public void testCancelNull() {
		expectedException.expect(NullPointerException.class);
		assertFalse(bank.cancelAccount(admin, null, 0));
		assertFalse(bank.cancelAccount(null, "Customer", 0));
	}

}
