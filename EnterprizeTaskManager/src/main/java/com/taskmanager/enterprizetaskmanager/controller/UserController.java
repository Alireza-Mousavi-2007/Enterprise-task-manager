package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.exceptions.UserNotFoundException;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserDetail(@PathVariable Integer id) {
        var user = userService.getUserById(id);
        if (user == null) throw new UserNotFoundException("There's no user with id = " + id);
        else
            return user;
    }

    @GetMapping("/by-email/{email}")
    public User getUserDetail(@PathVariable String email) {
        var user = userService.getUsrByEmail(email);
        if (user == null) throw new UserNotFoundException("There's no user with email = " + email);
        else
            return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserDetail(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetails(id, userDTO);
        return ResponseEntity.ok("user updated");

    }

    @PutMapping("/by-email/{email}")
    public ResponseEntity<String> updateUserDetail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetails(email, userDTO);
        return ResponseEntity.ok("user updated");

    }

}
