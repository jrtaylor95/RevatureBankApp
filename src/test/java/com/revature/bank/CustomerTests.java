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
	public ExpectedException expectedException = ExpectedException.none();
	
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
		Account expectedAccount = customer.getAccount(0);
		expectedAccount.withdraw(500);
		
		Account actualAccount = customer.getAccount(0);
		assertEquals(expectedAccount, actualAccount);
	}
}
