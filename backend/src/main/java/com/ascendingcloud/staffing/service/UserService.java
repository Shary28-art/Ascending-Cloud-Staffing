package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
