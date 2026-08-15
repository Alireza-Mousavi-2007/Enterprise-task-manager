package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.exceptions.UserNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.UserRepository;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(userDTO.getRole());

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
    public User updateUserDetails(Integer userId, UserDTO userDTO) {
        var user = repo.findById(userId);
        if (user.isPresent()){
            user.get().setUsername(userDTO.getUsername());
            user.get().setPassword(userDTO.getPassword());
            user.get().setEmail(userDTO.getEmail());
            user.get().setRoles(userDTO.getRole());
            user.get().setEnabled(userDTO.isEnabled());
            return user.get();
        }
        else throw new UserNotFoundException("There's no user with id = "+userId);
    }

    @Override
    public User updateUserDetails(String userEmail, UserDTO userDTO) {
        {
            var user = repo.findByEmail(userEmail);
            if (user== null){
                throw new UserNotFoundException("There's no user with email = "+userEmail);
            }
            else {
                user.setUsername(userDTO.getUsername());
                user.setPassword(userDTO.getPassword());
                user.setEmail(userDTO.getEmail());
                user.setRoles(userDTO.getRole());
                user.setEnabled(userDTO.isEnabled());

                return user;
            }

        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repo.getByUsername(username);
        if (user == null) throw new UserNotFoundException("there's no user with username : " + username);
        else
            return user;
    }
}
