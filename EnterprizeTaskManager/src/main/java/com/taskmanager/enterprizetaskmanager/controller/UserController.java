package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.exceptions.UserNotFoundException;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseEntity.ok("user " + userDTO.getUsername() + " added successfully !");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    @PreAuthorize("authentication.name==#username or hasRole('ADMIN')")
    public User getUserDetailByName(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/by-email/{email:.+}")
    @PreAuthorize("@userServiceImpl.areEmailAndUsernameSame(authentication.name,#email) or hasRole('ADMIN')")
    public User getUserDetailByEmail(@PathVariable String email) {
        System.out.println("current yser authorities : "+ SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return userService.getUsrByEmail(email);

    }

    @PutMapping("/{username}")
    @PreAuthorize("authentication.name==#username")
    public ResponseEntity<String> updateUserDetailByName(@PathVariable String username, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetailsByUsername(username, userDTO);
        return ResponseEntity.ok("user updated");

    }

    @PutMapping("/by-email/{email:.+}")
    @PreAuthorize("@userServiceImpl.areEmailAndUsernameSame(authentication.name,#email)")
    public ResponseEntity<String> updateUserDetail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetailsByEmail(email, userDTO);



        return ResponseEntity.ok("user updated");

    }

}
