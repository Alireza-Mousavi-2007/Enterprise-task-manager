package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.RoleDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.repository.AuthorityRepository;
import com.taskmanager.enterprizetaskmanager.repository.RoleRepository;
import com.taskmanager.enterprizetaskmanager.service.impl.AuthorityServiceImpl;
import com.taskmanager.enterprizetaskmanager.service.impl.RoleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private AuthorityService authorityService;

    private Authority authority;
    private Role role;

    @BeforeEach
    public void init() {

        authority = Authority.builder().id(1).authority("authority").build();

        role = Role.builder().id(1).role("role").authorities(Set.of(authority)).build();
    }

    @Test
    public void addRoleTest() {
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(authorityService.getAuthorityByName(any())).thenReturn(authority);

        RoleDTO dto = RoleDTO.builder().roleName("roleDto").authorities(Set.of(authority)).build();
        var tested = roleService.addRole(dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getRole()).isNotNull();
        Assertions.assertThat(tested.getAuthorities())
                .extracting(role -> role.getAuthority())
                .contains(authority.getAuthority());

        Mockito.verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void getRoleByNameTest() {
        when(roleRepository.getByRole(anyString())).thenReturn(role);

        var tested = roleService.getRoleByName(role.getRole());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getRole()).isNotNull();
        Assertions.assertThat(tested.getAuthorities())
                .extracting(a -> a.getAuthority())
                .contains(authority.getAuthority());

        Mockito.verify(roleRepository).getByRole(anyString());
    }

    @Test
    public void getAllRolesTest(){
        when(roleRepository.findAll()).thenReturn(List.of(role));

        var tested=roleService.getAllRoles();

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested).isNotEmpty();
        Assertions.assertThat(tested).contains(role);

        Mockito.verify(roleRepository).findAll();
    }

}
