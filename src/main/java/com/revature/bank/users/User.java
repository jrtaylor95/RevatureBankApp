package com.revature.bank.users;

import java.io.Serializable;

public abstract class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4552266511648650843L;
	private int id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	
	public User() {};
	
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
	
	public boolean checkPassword(String password) {
		return this.password.equals(password);
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

	/**
	 * @return the user's first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the user's last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
