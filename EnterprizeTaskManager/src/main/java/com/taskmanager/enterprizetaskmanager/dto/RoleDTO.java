package com.taskmanager.enterprizetaskmanager.dto;

import com.taskmanager.enterprizetaskmanager.entity.Authority;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class RoleDTO {
    @NotBlank(message = "Role title can't be empty")
    private String roleName;
    private Set<Authority> authorities;

    public RoleDTO(String roleName) {
        this.roleName = roleName;
    }

    public RoleDTO(String roleName, Set<Authority> authorities) {
        this.roleName = roleName;
        this.authorities = authorities;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
