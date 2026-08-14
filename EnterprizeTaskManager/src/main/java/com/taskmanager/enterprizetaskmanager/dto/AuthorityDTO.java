package com.taskmanager.enterprizetaskmanager.dto;

public class AuthorityDTO {

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
