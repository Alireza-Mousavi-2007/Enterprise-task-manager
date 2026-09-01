package com.taskmanager.enterprizetaskmanager.repository;

import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository repository;

    @Test
    public void getByRoleTest() {
        Authority authority = Authority.builder().authority("authority").build();
        authorityRepository.save(authority);
        Role role = Role.builder().role("role").authorities(Set.of(authority)).build();
        repository.save(role);

        var tested = repository.getByRole("role");

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getRole()).isEqualTo("role");
        Assertions.assertThat(tested.getAuthorities()).extracting(auth -> auth.getAuthority()).contains("authority");

    }
}
