package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.AuthorityDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;

import java.util.List;

public interface AuthorityService {

    public Authority addAuthority(AuthorityDTO authorityDTO);

    public List<Authority> getAllAuthorities();


}
