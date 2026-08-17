package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.RoleDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.exceptions.RoleNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.RoleRepository;
import com.taskmanager.enterprizetaskmanager.service.AuthorityService;
import com.taskmanager.enterprizetaskmanager.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository repo;
    private AuthorityService authorityService;

    public RoleServiceImpl(RoleRepository repo, AuthorityService authorityService) {
        this.repo = repo;
        this.authorityService = authorityService;
    }

    @Override
    public Role addRole(RoleDTO roleDTO) {

        Role role = new Role();
        role.setRole(roleDTO.getRoleName());

        Set<Authority> authorities = new HashSet<>();
        for (var a : roleDTO.getAuthorities()){
            authorities.add(authorityService.getAuthorityByName(a.getAuthority()));
        }

        role.setAuthorities(authorities);

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
