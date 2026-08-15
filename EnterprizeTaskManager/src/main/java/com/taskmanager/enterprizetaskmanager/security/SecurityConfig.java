package com.taskmanager.enterprizetaskmanager.security;

import com.taskmanager.enterprizetaskmanager.security.jwt.JwtFilter;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
            sfc.requestMatchers("TODO !!!!!!!!!!!!!!!").permitAll();
            sfc.anyRequest().authenticated();
        });

        security.addFilterBefore(jwtFilter, ExceptionTranslationFilter.class);

// TODO : add entrypoint and accessDenied Exceptions

        return security.build();
    }


    public AuthenticationManager authenticationManager(UserServiceImpl user) {

        var auth = new DaoAuthenticationProvider(user);
        auth.setPasswordEncoder(passwordEncoder());
        return new ProviderManager();

    }
}
