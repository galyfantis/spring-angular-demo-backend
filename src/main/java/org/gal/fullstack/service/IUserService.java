package org.gal.fullstack.service;

import java.util.List;

import org.gal.fullstack.entity.User;

public interface IUserService {
	
	User findByUserName(String username);
	
	List<User> getAll();

	boolean createUser(User user);
	
	void updateUser(User user);

	void deleteUser(String username);

	void enableUser(String username);
	
	void disableUser(String username);
}
