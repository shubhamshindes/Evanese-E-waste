package com.evanesce.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.evanesce.entity.User;
import com.evanesce.exception.BadRequestException;
import com.evanesce.exception.ResourceNotFoundException;
import com.evanesce.repository.UserDao;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // Insert / Register user
    @Override
    public User insertUser(User user) {

        if (userDao.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        userDao.findByPhone(user.getPhone())
                .ifPresent(u -> {
                    throw new BadRequestException("Phone number already exists");
                });

        return userDao.save(user);
    }

    // Login
    @Override
    public List<User> getUserByEmailAndPassword(String email, String password) {

        User user = userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid email or password");
        }

        return Collections.singletonList(user);
    }

    // Find by email
    @Override
    public List<User> findByEmail(String email) {

        User user = userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));

        return Collections.singletonList(user);
    }

    // Find by phone
    @Override
    public List<User> findByPhone(String phone) {

        User user = userDao.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with phone: " + phone));

        return Collections.singletonList(user);
    }

    // Forgot password
    @Override
    public List<User> forgetPassword(String email, String securityQues, String securityAns) {

        User user = userDao
                .findByEmailAndSecurityQuesAndSecurityAns(email, securityQues, securityAns)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Security details do not match"));

        return Collections.singletonList(user);
    }

    // Get all users
    @Override
    public List<User> getAllUsers() {

        List<User> users = userDao.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        return users;
    }

    // Update password
    @Override
    public User updatePassword(User user) {

        User existingUser = userDao.findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + user.getEmail()));

        existingUser.setPassword(user.getPassword());
        return userDao.save(existingUser);
    }

    // Delete user
    @Override
    public void deleteUser(String email) {

        User user = userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));

        userDao.delete(user);
    }
}
