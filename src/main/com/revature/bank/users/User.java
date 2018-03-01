package com.revature.bank.users;

public abstract class User {
	private final String userName;
	private String password;
	
	/**
	 * Constructs a user with the user name and password of userName, and password respectively.
	 * @param userName
	 * @param password
	 */
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * Returns the user name of this user.
	 * @return The user name of this user.
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * Returns the password of this user.
	 * @return The password of this user.
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Changes the password of the user to a new password.
	 * @param password
	 * @return True, if the password was able to be changed; false, if there was unable to be changed.
	 */
	public boolean setPassword(String password) {
		// TODO Implement password constraint check
		this.password = password;
		
		return true;
	}
	
	
}
