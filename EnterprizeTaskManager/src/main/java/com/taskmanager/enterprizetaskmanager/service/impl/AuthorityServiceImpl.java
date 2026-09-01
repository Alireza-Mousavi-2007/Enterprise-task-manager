package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.AuthorityDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.exceptions.AuthorityNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.AuthorityRepository;
import com.taskmanager.enterprizetaskmanager.service.AuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository repo;

    public AuthorityServiceImpl(AuthorityRepository repo) {
        this.repo = repo;
    }

    @Override
    public Authority getAuthorityByName(String name) {
        var authority = repo.getByAuthority(name);
        if (authority == null) throw new AuthorityNotFoundException("there's no authority with name : " + name);
        return authority;
    }

    @Override
    public Authority addAuthority(AuthorityDTO authorityDTO) {

        Authority authority = new Authority();
        authority.setAuthority(authorityDTO.getAuthorityName());

        return repo.save(authority);
    }


    @Override
    public List<Authority> getAllAuthorities() {
        return repo.findAll();
    }


}
