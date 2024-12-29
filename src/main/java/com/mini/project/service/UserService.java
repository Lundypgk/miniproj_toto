package com.mini.project.service;

import com.mini.project.model.User;
import com.mini.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void addUser(User user) {
    if (userRepository.getUserById(user.getUserId()) != null) {
      throw new IllegalArgumentException("User with this username already exists.");
    }
    if (user.getUserId() == null || user.getUsername() == null || user.getPassword() == null) {
      throw new IllegalArgumentException("User ID, username, and password are required.");
    }
    userRepository.saveUser(user);
  }

  public User getUserById(String userId) {
    User user = userRepository.getUserById(userId);
    if (user == null) {
      throw new IllegalArgumentException("User not found for ID: " + userId);
    }
    return user;
  }

  public boolean usernameExists(String username) {
    String userId = "user:" + username;
    return userRepository.getUserById(userId) != null;
  }
}
