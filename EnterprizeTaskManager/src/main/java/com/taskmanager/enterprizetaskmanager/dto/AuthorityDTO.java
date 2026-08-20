package com.taskmanager.enterprizetaskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorityDTO {

    @NotBlank(message = "authority title can't be empty")
    private String authorityName;

    public AuthorityDTO(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
