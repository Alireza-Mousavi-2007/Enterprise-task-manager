package com.taskmanager.enterprizetaskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authorities")
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @Column(name = "authority_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "authority_title")
    @NotBlank(message = "authority title can't be empty")
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority(Integer id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
