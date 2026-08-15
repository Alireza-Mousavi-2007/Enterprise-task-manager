package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.RoleDTO;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.exceptions.RoleNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.RoleRepository;
import com.taskmanager.enterprizetaskmanager.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository repo;

    public RoleServiceImpl(RoleRepository repo) {
        this.repo = repo;
    }

    @Override
    public Role addRole(RoleDTO roleDTO) {

        Role role = new Role();
        role.setRole(roleDTO.getRoleName());
        role.setAuthorities(roleDTO.getAuthorities());

        return repo.save(role);
    }

    @Override
    public Role getRoleByName(String name) {

        var user = repo.getByRole(name);
        if (user == null) throw new RoleNotFoundException("There's no role with name : " + name);

        return user;
    }

    @Override
    public List<Role> getAllRoles() {
        return repo.findAll();
    }
}
