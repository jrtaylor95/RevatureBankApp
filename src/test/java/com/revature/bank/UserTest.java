package com.revature.bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.revature.bank.users.Customer;
import com.revature.bank.users.User;

public class UserTest {

	User customer;

	@Before
	public void setUp() throws Exception {
		customer = new Customer("Customer", "Password");
	}

	@Test
	public void testGetUserInformation() {
		assertEquals("Customer", customer.getUserName());
		assertEquals("Password", customer.getPassword());
	}
	
	@Test
	public void testSetUserInformation() {
		customer.setFirstName("Michael");
		assertEquals("Michael", customer.getFirstName());
		customer.setLastName("Bluth");
		assertEquals("Bluth", customer.getLastName());
		assertTrue(customer.setPassword("pswd"));
		assertEquals("pswd", customer.getPassword());
	}

}
