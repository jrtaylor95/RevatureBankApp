package com.revature.bank.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<T> {
	public void create(T t) throws SQLException;
	
	public T select(int id) throws SQLException;
	
	public List<T> selectAll() throws SQLException;
	
	public void update(T t) throws SQLException;
	
	public void delete(int id) throws SQLException;
}
