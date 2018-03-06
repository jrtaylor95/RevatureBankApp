package com.revature.bank;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AccountTests.class, BankTests.class, CustomerTests.class, UserTest.class})
public class AllTests {

}
