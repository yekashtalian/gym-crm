package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
  @PersistenceContext private EntityManager entityManager;

  @Override
  public Optional<User> findByUsername(String username) {
    try {
      var user =
          entityManager
              .createQuery("select u from User u where u.username = :username", User.class)
              .setParameter("username", username)
              .getSingleResult();
      return Optional.of(user);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
