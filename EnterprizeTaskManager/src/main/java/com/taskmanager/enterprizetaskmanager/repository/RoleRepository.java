package com.taskmanager.enterprizetaskmanager.repository;

import com.taskmanager.enterprizetaskmanager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Role getByRole(String roleName);
}
