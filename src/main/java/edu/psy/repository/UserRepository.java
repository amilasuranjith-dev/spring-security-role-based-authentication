package edu.psy.repository;

import edu.psy.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    int createUser(User user);
    int activateUser(Long id);
    int deactivateUser(Long id);
}
