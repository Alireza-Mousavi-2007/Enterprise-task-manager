package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.RoleDTO;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface RoleService {

    public Role addRole(RoleDTO roleDTO);

    public Role getRoleByName(String name);

    public List<Role> getAllRoles();
}
