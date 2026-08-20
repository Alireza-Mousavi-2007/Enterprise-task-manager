package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserProfileUpdateDTO;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.exceptions.UserNotFoundException;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "for controlling users ")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "add User by UserDTO",description = "UserDTO contains (private String username ,private String email ,private String password, private boolean enabled = true ,private Set<Role> role")
    public ResponseEntity<String> addUser(@Valid  @RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseEntity.ok("user " + userDTO.getUsername() + " added successfully !");
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    @Operation(summary = "get user  by username")
    @PreAuthorize("authentication.name==#username or hasRole('ADMIN')")
    public User getUserDetailByName(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/by-email/{email:.+}")
    @Operation(summary = "get user  by email")
    @PreAuthorize("@userServiceImpl.areEmailAndUsernameSame(authentication.name,#email) or hasRole('ADMIN')")
    public User getUserDetailByEmail(@PathVariable String email) {
        return userService.getUsrByEmail(email);

    }

    @PutMapping("/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update user  by admin with username")
    public ResponseEntity<String> updateUserDetailByName(@PathVariable String username, @Valid @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetailsByUsername(username, userDTO);
        return ResponseEntity.ok("user updated");

    }

    @PutMapping("/admin/by-email/{email:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update user  by admin with email")
    public ResponseEntity<String> updateUserDetail(@PathVariable String email,@Valid  @RequestBody UserDTO userDTO) {
        var updatedUser = userService.updateUserDetailsByEmail(email, userDTO);
        return ResponseEntity.ok("user updated");
    }

    @PutMapping("/{username}")
    @PreAuthorize("authentication.name==#username")
    @Operation(summary = "update user by himself with username")
    public ResponseEntity<String> updateUserProfileByHimselfWithUsername(@PathVariable String username ,@Valid @RequestBody UserProfileUpdateDTO userProfileUpdateDTO){
        var updated= userService.selfUpdateUserByUsername(username ,userProfileUpdateDTO);
        return ResponseEntity.ok("userUpdated");

    }

}
