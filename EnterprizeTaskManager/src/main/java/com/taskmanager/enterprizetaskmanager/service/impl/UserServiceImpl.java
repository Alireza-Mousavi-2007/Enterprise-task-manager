package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.RegisterDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.exceptions.UserNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.UserRepository;
import com.taskmanager.enterprizetaskmanager.service.RoleService;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repo;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User addUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> realRoles = new HashSet<>();
        for (Role r : userDTO.getRole()) {
            // to recognize role from database
            realRoles.add(roleService.getRoleByName(r.getRole()));
        }
        user.setRoles(realRoles);

        return repo.save(user);
    }

    @Override
    public User getByUsername(String username) {
        var user = repo.getByUsername(username);
        if (user == null) throw new UserNotFoundException("there's no user with username : " + username);
        else
            return user;
    }

    @Override
    public User getUsrByEmail(String email) {
        var user = repo.findByEmail(email);
        if (user == null) throw new UserNotFoundException("There's no user with email : " + email);
        else
            return user;
    }

    @Override
    public User getUserById(Integer id) {
        var user = repo.findById(id);
        if (user.isPresent()) return user.get();
        else throw new UserNotFoundException("There's no user with id = " + id);
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public User updateUserDetailsByUsername(String username, UserDTO userDTO) {
        var user = repo.getByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("There's no user with username = " + username);
        } else {
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            user.setRoles(userDTO.getRole());
            user.setEnabled(userDTO.isEnabled());

            repo.save(user);
            return user;
        }
    }

    @Override
    public User updateUserDetailsByEmail(String userEmail, UserDTO userDTO) {
        {
            var user = repo.findByEmail(userEmail);
            if (user == null) {
                throw new UserNotFoundException("There's no user with email = " + userEmail);
            } else {
                user.setUsername(userDTO.getUsername());
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setEmail(userDTO.getEmail());
                user.setRoles(userDTO.getRole());
                user.setEnabled(userDTO.isEnabled());

                repo.save(user);
                return user;
            }

        }
    }

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        var user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(roleService.getRoleByName("USER")));
        return repo.save(user);
    }

    @Override
    public boolean areEmailAndUsernameSame(String username, String email) {
        var user = repo.getByUsername(username);
        if (user == null) return false;
        return user.getEmail().equalsIgnoreCase(email);
    }


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        var user = repo.getByUsername(usernameOrEmail);
        if (user == null) user = getUsrByEmail(usernameOrEmail);
        if (user == null)
            throw new UserNotFoundException("there's no user with  : " + usernameOrEmail);
        else
            return user;
    }
}
