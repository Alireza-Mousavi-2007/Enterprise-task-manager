package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.AuthorityDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.repository.AuthorityRepository;
import com.taskmanager.enterprizetaskmanager.service.impl.AuthorityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorityServiceTest {
    @Mock
    private AuthorityRepository authorityRepository;
    @InjectMocks
    private AuthorityServiceImpl authorityService;

    private Authority authority;

    @BeforeEach
    public void init() {
        authority = Authority.builder()
                .id(1)
                .authority("authority").build();
    }

    @Test
    public void getAuthorityByNameTest() {

        when(authorityRepository.getByAuthority("authority")).thenReturn(authority);

        var tested = authorityService.getAuthorityByName("authority");

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getAuthority()).isEqualTo("authority");
        Mockito.verify(authorityRepository).getByAuthority("authority");

    }

    @Test
    public void addAuthorityTest() {
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);

        AuthorityDTO dto = new AuthorityDTO("authority");
        var tested = authorityService.addAuthority(dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getAuthority()).isEqualTo("authority");
        Mockito.verify(authorityRepository).save(any(Authority.class));
    }

    @Test
    public void getAllAuthoritiesTest() {
        when(authorityRepository.findAll()).thenReturn(List.of(authority));

        var tested = authorityService.getAllAuthorities();

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested).contains(authority);
        Assertions.assertThat(tested.size()).isEqualTo(1);
        Mockito.verify(authorityRepository).findAll();
    }
}
