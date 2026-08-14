package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public UserDTO addUser(UserDTO user);

    public User getUsrByEmail(String emil);

    public User getUserById(Integer id );

    public List<User> getAllUsers();

    public boolean isExistsByUsername(String username);

    public boolean isExistsByEmail(String email);


}
