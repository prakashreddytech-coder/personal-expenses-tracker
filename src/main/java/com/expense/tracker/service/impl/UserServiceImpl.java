package com.expense.tracker.service.impl;

import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        user.setStatus("ACTIVE");
        if (user.getRole() == null) {
            if (userRepository.count() == 0) {
                user.setRole("ADMIN");
            } else {
                user.setRole("USER");
            }
        }
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        return null;
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id, boolean permanent) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (permanent) {
            userRepository.delete(user);
        } else {
            user.setStatus("DEACTIVATED");
            userRepository.save(user);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
