package org.example.gymcrm.dao;

import org.example.gymcrm.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
}
