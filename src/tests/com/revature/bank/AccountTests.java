package com.revature.bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.bank.accounts.Account;

public class AccountTests {

	Account account;
	Account transferAccount;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		account = new Account("Checking");
		account.deposit(500);
		transferAccount = new Account("Checking");
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(500, account.getBalance(), 0);
	}
	
	@Test
	public void testGetNickName() {
		assertEquals("Checking", account.getNickName());
	}
	
	@Test
	public void testSetNickName() {
		account.setNickName("Checking - 2");
		assertEquals("Checking - 2", account.getNickName());
	}
	
	@Test
	public void testDeposit() {
		assertTrue(account.deposit(250));
		assertEquals(750, account.getBalance(), 0);
	}
	
	@Test
	public void testWithdraw() {
		assertTrue(account.withdraw(250));
		assertEquals(250, account.getBalance(), 0);
	}

	@Test
	public void testNegativeDeposit() {
		expectedException.expect(IllegalArgumentException.class);
		account.deposit(-250);
	}
	
	@Test
	public void testNegativeWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		account.deposit(-250);
	}
	
	@Test
	public void testOverWithdraw() {
		expectedException.expect(IllegalArgumentException.class);
		account.withdraw(750);
	}
}
