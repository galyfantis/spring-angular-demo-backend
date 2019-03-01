package org.gal.fullstack.dao;

import java.util.List;

import org.gal.fullstack.entity.User;

public interface IUserDAO {
	
	User get(String username);
	
	List<User> list();

	boolean userExists(String username);

	void createUser(User user);

	void updateUser(User user);

	void deleteUser(String username);
}
