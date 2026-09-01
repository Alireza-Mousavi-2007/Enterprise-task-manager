package com.taskmanager.enterprizetaskmanager.repository;

import com.taskmanager.enterprizetaskmanager.entity.Authority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void testGetByAuthority() {
        Authority authority = Authority.builder().authority("test").build();
        authorityRepository.save(authority);

        var tested = authorityRepository.getByAuthority("test");

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getAuthority()).isEqualTo("test");

    }

}
