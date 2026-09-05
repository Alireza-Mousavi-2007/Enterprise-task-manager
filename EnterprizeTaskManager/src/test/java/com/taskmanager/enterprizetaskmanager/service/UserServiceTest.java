package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.RegisterDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserProfileUpdateDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.repository.UserRepository;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RoleService roleService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;
    private Authority authority;

    @BeforeEach
    public void init() {
        authority = Authority.builder().id(1).authority("authority").build();
        role = Role.builder().id(1).role("role").authorities(Set.of(authority)).build();
        user = User.builder().id(1).username("username").email("example@email.com").password("password").enabled(true).roles(Set.of(role)).build();
    }

    @Test
    public void addUserTest() {
        when(roleService.getRoleByName(anyString())).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserDTO dto = UserDTO.builder()
                .username("username")
                .email("example@email.com")
                .password("password")
                .enabled(true)
                .role(Set.of(role)).build();

        var tested = userService.addUser(dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getPassword()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(Role::getRole).contains(role.getRole());
        Assertions.assertThat(tested.getAuthorities()).extracting(auth -> auth.getAuthority()).contains(authority.getAuthority());

        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void getByUsernameTest() {

        when(userRepository.getByUsername(anyString())).thenReturn(user);

        var tested = userService.getByUsername(user.getUsername());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(r -> r.getRole()).contains("role");
        Assertions.assertThat(tested.getAuthorities()).extracting(a -> a.getAuthority()).contains("authority");

        Mockito.verify(userRepository).getByUsername(anyString());
    }

    @Test
    public void getUserByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        var tested = userService.getUsrByEmail(user.getEmail());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(r -> r.getRole()).contains("role");
        Assertions.assertThat(tested.getAuthorities()).extracting(a -> a.getAuthority()).contains("authority");

        Mockito.verify(userRepository).findByEmail(anyString());
    }

    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        var tested = userService.getUserById(user.getId());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(r -> r.getRole()).contains("role");
        Assertions.assertThat(tested.getAuthorities()).extracting(a -> a.getAuthority()).contains("authority");

        Mockito.verify(userRepository).findById(anyInt());
    }

    @Test
    public void getAllUsersTest() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var tested = userService.getAllUsers();

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.size()).isGreaterThan(0);
        Assertions.assertThat(tested).contains(user);

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void isExistsByUsernameTest() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        var tested = userService.isExistsByUsername(user.getUsername());

        Assertions.assertThat(tested).isTrue();

        Mockito.verify(userRepository).existsByUsername(anyString());
    }

    @Test
    public void isExistsByEmailTest() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        var tested = userService.isExistsByEmail(user.getEmail());

        Assertions.assertThat(tested).isTrue();

        Mockito.verify(userRepository).existsByEmail(anyString());
    }

    @Test
    public void updateUserDetailsByUsernameTest() {
        when(userRepository.getByUsername(anyString())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleService.getRoleByName(anyString())).thenReturn(role);

        UserDTO dto = UserDTO.builder()
                .username("username")
                .email("example@email.com")
                .password("password")
                .enabled(true)
                .role(Set.of(role)).build();

        var tested = userService.updateUserDetailsByUsername(user.getUsername(), dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getPassword()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(r -> r.getRole()).contains(role.getRole());
        Assertions.assertThat(tested.getAuthorities()).extracting(auth -> auth.getAuthority()).contains(authority.getAuthority());

        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userRepository).getByUsername(anyString());
    }

    @Test
    public void updateUserDetailsByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleService.getRoleByName(anyString())).thenReturn(role);

        UserDTO dto = UserDTO.builder()
                .username("username")
                .email("example@email.com")
                .password("password")
                .enabled(true)
                .role(Set.of(role)).build();

        var tested = userService.updateUserDetailsByEmail(user.getEmail(), dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getPassword()).isNotEmpty();
        Assertions.assertThat(tested.getRoles()).extracting(Role::getRole).contains(role.getRole());
        Assertions.assertThat(tested.getAuthorities()).extracting(auth -> auth.getAuthority()).contains(authority.getAuthority());

        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userRepository).findByEmail(anyString());
    }


    @Test
    public void selfUpdateUserByUsernameTest() {
        when(userRepository.getByUsername(anyString())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        var dto = UserProfileUpdateDTO.builder()
                .username("username")
                .email("example@email.com")
                .password("password").build();

        var tested = userService.selfUpdateUserByUsername(user.getUsername(), dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getPassword()).isNotEmpty();

        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userRepository).getByUsername(anyString());
        Mockito.verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void registerUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleService.getRoleByName(anyString())).thenReturn(role);
        var dto = RegisterDTO.builder().username("username").
                email("example@email.com")
                .password("password").build();

        var tested = userService.registerUser(dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getUsername()).isNotEmpty();
        Assertions.assertThat(tested.getEmail()).isNotEmpty();
        Assertions.assertThat(tested.getPassword()).isNotEmpty();

        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void areEmailAndUsernameSameTest() {
        when(userRepository.getByUsername(user.getUsername())).thenReturn(user);

        UserDTO wrong = UserDTO.builder()
                .username("wrong")
                .email("wrong@email.com")
                .password("wrong")
                .enabled(true)
                .role(Set.of(role)).build();

        var tested = userService.areEmailAndUsernameSame(user.getUsername(),user.getEmail());
        var falseTested1 = userService.areEmailAndUsernameSame(user.getUsername(),wrong.getEmail());
        var falseTested2 = userService.areEmailAndUsernameSame(wrong.getUsername(),user.getEmail());


        Assertions.assertThat(tested).isTrue();
        Assertions.assertThat(falseTested1).isFalse();
        Assertions.assertThat(falseTested2).isFalse();

        Mockito.verify(userRepository,times(3)).getByUsername(anyString());
    }

    //gonna correct name of test methods from now


    @Test
    public void loadUserByUsername_whenUserExists_userDetail(){
        when((userRepository.getByUsername(user.getUsername()))).thenReturn(user);
        when((userRepository.getByUsername(user.getEmail()))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(user);


        var testedUsername = userService.loadUserByUsername(user.getUsername());
        var testedEmail = userService.loadUserByUsername(user.getEmail());

        Assertions.assertThat(testedUsername).isNotNull();
        Assertions.assertThat(testedUsername.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(testedUsername.getAuthorities()).isEqualTo(user.getAuthorities());


        Assertions.assertThat(testedEmail).isNotNull();
        Assertions.assertThat(testedEmail.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(testedEmail.getAuthorities()).isEqualTo(user.getAuthorities());

        Mockito.verify(userRepository,times(2)).getByUsername(anyString());
        Mockito.verify(userRepository).findByEmail(anyString());
    }


}
