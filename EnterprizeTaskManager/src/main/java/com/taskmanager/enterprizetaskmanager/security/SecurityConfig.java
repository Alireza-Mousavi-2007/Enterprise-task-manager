package com.taskmanager.enterprizetaskmanager.security;

import com.taskmanager.enterprizetaskmanager.dto.AuthorityDTO;
import com.taskmanager.enterprizetaskmanager.dto.RoleDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.security.jwt.JwtFilter;
import com.taskmanager.enterprizetaskmanager.service.AuthorityService;
import com.taskmanager.enterprizetaskmanager.service.RoleService;
import com.taskmanager.enterprizetaskmanager.service.TaskService;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public CommandLineRunner runner(AuthorityService authorityService,
                                    RoleService roleService,
                                    TaskService taskService,
                                    UserService userService) {

        return args -> {
            if (userService.isExistsByEmail("alirezamousaviseyed1386@gmail.com")) return;

            var create = authorityService.addAuthority(new AuthorityDTO("create"));
            var read = authorityService.addAuthority(new AuthorityDTO("read"));
            var update = authorityService.addAuthority(new AuthorityDTO("update"));
            var delete = authorityService.addAuthority(new AuthorityDTO("delete"));

            var admin = roleService.addRole(new RoleDTO("ROLE_ADMIN", Set.of(create, read, update, delete)));
            var user = roleService.addRole(new RoleDTO("ROLE_USER", Set.of(read)));

            var alireza = userService.addUser(new UserDTO("alireza",
                    "alirezamousaviseyed1386@gmail.com", "password", true, Set.of(admin)));

        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) {

        security.csrf(csrf -> csrf.disable());

        security.authorizeHttpRequests(sfc -> {
            sfc.requestMatchers("/api/auth/**").permitAll();
            sfc.anyRequest().authenticated();
        });

        security.addFilterBefore(jwtFilter, ExceptionTranslationFilter.class);

// TODO : add entrypoint and accessDenied Exceptions

        return security.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(UserServiceImpl user) {

        var auth = new DaoAuthenticationProvider(user);
        auth.setPasswordEncoder(passwordEncoder());
        return new ProviderManager();

    }
}
