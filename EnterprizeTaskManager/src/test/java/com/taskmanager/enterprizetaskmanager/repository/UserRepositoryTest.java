package com.taskmanager.enterprizetaskmanager.repository;

import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    private Authority authority;
    private Role role;
    private User user;

    @BeforeEach
    public void init() {
        authority = Authority.builder()
                .authority("authority").build();
        authorityRepository.save(authority);

        role = Role.builder()
                .role("role")
                .authorities(Set.of(authority)).build();
        roleRepository.save(role);

        user = User.builder()
                .username("username")
                .email("example@email.com")
                .password("password")
                .enabled(true)
                .roles(Set.of(role)).build();
        userRepository.save(user);
    }

    @Test
    public void findByEmailTest() {

        var tested = userRepository.findByEmail("example@email.com");

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getEmail()).isEqualTo("example@email.com");
        Assertions.assertThat(tested.getUsername()).isEqualTo("username");
        Assertions.assertThat(tested.getPassword()).isEqualTo("password");
        Assertions.assertThat(tested.getRoles()).extracting(role -> role.getRole()).contains("role");

    }

    @Test
    public void existsByUsernameTest() {
        var tested = userRepository.existsByUsername("username");
        var wrongTested = userRepository.existsByUsername("wrong");
        Assertions.assertThat(tested).isTrue();
        Assertions.assertThat(wrongTested).isFalse();

    }

    @Test
    public void existsByEmailTest() {
        var tested = userRepository.existsByEmail("example@email.com");
        var wrongTested = userRepository.existsByEmail("wrong");
        Assertions.assertThat(tested).isTrue();
        Assertions.assertThat(wrongTested).isFalse();

    }

    @Test
    public void getByUsernameTest() {
        var tested = userRepository.getByUsername("username");

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getEmail()).isEqualTo("example@email.com");
        Assertions.assertThat(tested.getUsername()).isEqualTo("username");
        Assertions.assertThat(tested.getPassword()).isEqualTo("password");
        Assertions.assertThat(tested.getRoles()).extracting(role -> role.getRole()).contains("role");
    }
}
