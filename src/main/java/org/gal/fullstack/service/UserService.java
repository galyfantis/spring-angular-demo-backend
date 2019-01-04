package org.gal.fullstack.service;

import java.util.List;
import java.util.Optional;

import org.gal.fullstack.controller.UserNotFoundException;
import org.gal.fullstack.dao.IUserDAO;
import org.gal.fullstack.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private IUserDAO userDAO;

	@Override
	public User findByUserName(String username) {
		return userDAO.get(username);
	}

	@Override
	public List<User> getAll() {
		return userDAO.list();
	}
	
	@Override
	public synchronized boolean createUser(User user){
       if (userDAO.userExists(user.getUsername())) {
    	   return false;
       } else {
    	   userDAO.createUser(user);
    	   return true;
       }
	}

	@Override
	public void updateUser(User user) {
		userDAO.updateUser(user);
	}

	@Override
	public void deleteUser(String username) {
		userDAO.deleteUser(username);
	}

	@Override
	public void enableUser(String username) {
		User user = Optional.ofNullable(userDAO.get(username)).orElseThrow(UserNotFoundException::new);
		user.setEnabled(true);
		userDAO.updateUser(user);
	}
	
	@Override
	public void disableUser(String username) {
		User user = Optional.ofNullable(userDAO.get(username)).orElseThrow(UserNotFoundException::new);
		user.setEnabled(false);
		userDAO.updateUser(user);
	}

}
