package org.gal.fullstack.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gal.fullstack.controller.UserNotFoundException;
import org.gal.fullstack.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDAO implements IUserDAO {
	
	@PersistenceContext	
	private EntityManager entityManager;	

	@Override
	public User get(String username) {
		return entityManager.find(User.class, username);
	}

	@Override
	public List<User> list() {
		String hql = "FROM User as usr ORDER BY usr.username ASC";
		return (List<User>) entityManager.createQuery(hql).getResultList();
	}

	@Override
	public boolean userExists(String username) {
		String hql = "FROM User as usr WHERE usr.username = ?";
		int count = entityManager.createQuery(hql).setParameter(1, username)
		              .getResultList().size();
		return count > 0 ? true : false;
	}

	@Override
	public void createUser(User user) {
		entityManager.persist(user);
	}

	@Override
	public void updateUser(User user) {
		entityManager.flush();
	}

	@Override
	public void deleteUser(String username) {
		User user = Optional.ofNullable(get(username)).orElseThrow(UserNotFoundException::new);
		entityManager.remove(user);
	}
}
