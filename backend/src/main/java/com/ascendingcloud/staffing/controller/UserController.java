package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }
}