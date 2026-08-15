package com.taskmanager.enterprizetaskmanager.repository;

import com.taskmanager.enterprizetaskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String emil);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    User findByUsername(String username);
}
