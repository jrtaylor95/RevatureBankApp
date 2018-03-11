package com.revature.bank;

import static org.junit.Assert.*;

import java.sql.SQLException;

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
	private Customer transferCustomer;
	private Administrator admin;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		try {
			customer = (Customer) bank.register("Customer", "Password", "Michael", "Bluth");
			transferCustomer = (Customer) bank.register("Transfer", "Password", "Linday", "Bluth Funke");
			
		} catch (SQLException e) {
			customer = (Customer) bank.logon("Customer", "Password");
			transferCustomer = (Customer) bank.logon("Transfer", "Password");
		};
		Account transferAccount = new Account("Checking");
		transferCustomer.addAccount(transferAccount);
		
		Account expectedAccount = new Account("Checking");
		expectedAccount.deposit(500);
		customer.addAccount(expectedAccount);
		admin = (Administrator) bank.registerEmployee("Admin", "Password", true,  "Gob", "Bluth");
	}

	@Test
	public void testRegisterLogon() {
		try {
			Customer expectedCustomer = (Customer) bank.register("New Customer", "Password", "Michael", "Bluth");
			assertEquals(2, bank.getCustomers().size());
			Customer actualCustomer = (Customer) bank.logon("New Customer", "Password");
			assertEquals(expectedCustomer, actualCustomer);
		} catch (SQLException e) {};
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
	public void testDuplicateRegister(){
		Customer duplicateCustomer;
		try {
			duplicateCustomer = (Customer) bank.register("Customer", "Password", "Michael", "Bluth");
			assertNull(duplicateCustomer);
			assertEquals(1, bank.getCustomers().size());
		} catch (SQLException e) {}
	}

	@Test
	public void testCustomerRegisterWithEmployee() {
		try {
			Customer expectedCustomer = (Customer) bank.register("Admin", "Password", "Gob", "Bluth");
			assertNull(expectedCustomer);
			assertEquals(1, bank.getCustomers().size());
		} catch (SQLException e) {}
	}

	@Test
	public void testCustomerRegisterWithNull() {
		try {
			expectedException.expect(NullPointerException.class);
			bank.register(null, "Password", "Michael", "Bluth");
		} catch (SQLException e) {}
	}

	@Test
	public void testRegisterEmployee() throws SQLException {
		Employee expectedCustomer = (Employee) bank.registerEmployee("Employee", "Password", true,  "Mae", "Funke");
		assertNotNull(expectedCustomer);
		assertEquals(2, bank.getEmployees().size());
	}

	@Test
	public void testRegisterDuplicateEmployee() throws SQLException {
		Employee expectedEmployee = (Administrator) bank.registerEmployee("Admin", "Password", true,  "Gob", "Bluth");
		assertNull(expectedEmployee);
		assertEquals(1, bank.getEmployees().size());
	}

	@Test
	public void testRegisterEmployeeWithCustomer() throws SQLException {
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
		try {
			assertTrue(bank.transfer(customer, 0, "Transfer", 0, 250));
			Account actualAccount = customer.getAccount(0);
			Account actualTransferAccount = transferCustomer.getAccount(0);
			assertEquals(250, actualAccount.getBalance(), 0);
			assertEquals(750, actualTransferAccount.getBalance(), 0);
		} catch (SQLException e) {}
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
		try {
			expectedException.expect(IllegalArgumentException.class);
			bank.transfer(customer, 0, "Transfer", 0, -100);
		} catch (SQLException e) {}
	}

	@Test
	public void testOverWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		bank.withdraw(customer, 0, 750);
	}

	@Test
	public void testOverTransfer() throws IllegalArgumentException, SQLException {
			expectedException.expect(IllegalArgumentException.class);
			bank.transfer(customer, 0, "Transfer", 0, 750);
	}

	@Test
	public void testTransferToNull() throws IllegalArgumentException, SQLException {
		expectedException.expect(NullPointerException.class);
			bank.transfer(customer, 0, null, 0, 500);
	}

	@Test 
	public void testTransferToNoAccount() {
		try {
			assertNotNull(bank.register("Transfer", "Password", "Linday", "Bluth Funke"));
			expectedException.expect(IndexOutOfBoundsException.class);
			bank.transfer(customer, 0, "Transfer", 0, 500);
		} catch (SQLException e) {}
	}

	@Test
	public void testTransferFromOutOfBounds() {
		try {
			expectedException.expect(IndexOutOfBoundsException.class);
			bank.transfer(customer, 5, "Transfer", 0, 500);
		} catch (SQLException e) {}
	}

	@Test
	public void testTransferToOutOfBounds() {
		try {
			expectedException.expect(IndexOutOfBoundsException.class);
			bank.transfer(customer, 0, "Transfer", 5, 500);
		} catch (SQLException e) {}
	}

	@Test
	public void testAdminWithdraw() {
		try {
		assertTrue(bank.withdraw(admin, "Customer", 0, 250));
		Account actualAccount = customer.getAccount(0);
		assertEquals(250, actualAccount.getBalance(), 0);
		} catch (SQLException e) {}
	}

	@Test
	public void testAdminDeposit() {
		try {
		assertTrue(bank.deposit(admin, "Customer", 0, 250));
		Account actualAccount = customer.getAccount(0);
		assertEquals(750, actualAccount.getBalance(), 0);
		} catch (SQLException e) {}
	}

	@Test
	public void testAdminTransferSameCustomer() {
		try {
		Account transferAccount = new Account("Checking - 2");
		customer.addAccount(transferAccount);

		assertTrue(bank.transfer(admin, "Customer", 0, 1, 250));
		Account expectedAccount = customer.getAccount(0);
		assertEquals(250, expectedAccount.getBalance(), 0);
		assertEquals(250, transferAccount.getBalance(), 0);
		} catch (SQLException e) {}
	}

	@Test
	public void testAdminTransferDifferentCustomer() throws SQLException {
		assertTrue(bank.transfer(admin, "Customer", 0, "Transfer", 0, 250));
		Account expectedAccount = customer.getAccount(0);
		assertEquals(250, expectedAccount.getBalance(), 0);
		Account expectedTransferAccount = transferCustomer.getAccount(0);
		assertEquals(250, expectedTransferAccount.getBalance(), 0);
	}

	@Test
	public void testApproveAccount() throws SQLException {
		bank.apply(customer, "Checking - 2");
		assertEquals(1, bank.getPendingAccounts().size());
		bank.approveAccount(admin, customer.getUserName());
		assertEquals(2, customer.getAccounts().size());
		assertEquals(0, bank.getPendingAccounts().size());
	}

	@Test
	public void testApproveAccountNull() throws SQLException {
		expectedException.expect(NullPointerException.class);
		bank.approveAccount(admin, null);
	}

	@Test
	public void testApproveAccountNoCustomer() throws SQLException {
		assertFalse(bank.approveAccount(admin, "Customer"));
	}

	@Test
	public void testRejectAccount() throws SQLException {
		bank.apply(customer, "Checking - 2");
		assertEquals(1, bank.getPendingAccounts().size());
		bank.rejectAccount(admin, customer.getUserName());
		assertEquals(1, customer.getAccounts().size());
		assertEquals(0, bank.getPendingAccounts().size());
	}

	@Test
	public void testCancelAccount() throws SQLException {
		assertTrue(bank.cancelAccount(admin, "Customer", 0));

		assertTrue(customer.getAccounts().isEmpty());
	}

	@Test
	public void testCancelAccountOutOfBounds() throws SQLException {
		expectedException.expect(IndexOutOfBoundsException.class);
		bank.cancelAccount(admin, "Customer", 1);
	}

	@Test
	public void testCancelNull() throws SQLException {
		expectedException.expect(NullPointerException.class);
		assertFalse(bank.cancelAccount(admin, null, 0));
		assertFalse(bank.cancelAccount(null, "Customer", 0));
	}

}
