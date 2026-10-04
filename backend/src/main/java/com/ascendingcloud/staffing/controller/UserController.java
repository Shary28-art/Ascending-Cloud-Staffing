package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.UserDTO;
import com.ascendingcloud.staffing.mapper.UserMapper;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    public UserController(
            UserService userService,
            CurrentUserService currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    /** The logged-in user. */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {
        return ResponseEntity.ok(UserMapper.toDTO(currentUserService.getUser()));
    }

    /** Admin lookup by e-mail address. */
    @GetMapping("/email/{email:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserMapper.toDTO(userService.findByEmail(email)));
    }
}
