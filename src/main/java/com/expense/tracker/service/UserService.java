package com.expense.tracker.service;

import com.expense.tracker.entity.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User loginUser(String email, String password);
    User updateUser(Long id, User user);
    void deleteUser(Long id, boolean permanent);
    Optional<User> getUserById(Long id);
}
