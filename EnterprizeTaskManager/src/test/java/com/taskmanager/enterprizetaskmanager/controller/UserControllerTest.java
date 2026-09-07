package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserProfileUpdateDTO;
import com.taskmanager.enterprizetaskmanager.entity.Authority;
import com.taskmanager.enterprizetaskmanager.entity.Role;
import com.taskmanager.enterprizetaskmanager.entity.User;
import com.taskmanager.enterprizetaskmanager.security.jwt.JwtToken;
import com.taskmanager.enterprizetaskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtToken jwtToken;
    @MockitoBean
    private UserDetailsService userDetailsService;


    private User user;
    private Role role;
    private Authority authority;

    @BeforeEach
    public void init() {
        authority = Authority.builder().id(1).authority("authority").build();
        role = Role.builder().id(1).role("role").authorities(Set.of(authority)).build();
        user = User.builder().id(1).username("alireza").email("example.email.com").password("password").roles(Set.of(role)).enabled(true).build();
    }

    @Test
    @WithMockUser(username = "alireza", roles = {"ADMIN"})
    public void addUser_post_responseEntity() throws Exception {
        when(userService.addUser(any(UserDTO.class))).thenReturn(user);

        UserDTO dto = UserDTO.builder()
                .username("alireza")
                .email("example@email.com")
                .password("password")
                .role(Set.of(role))
                .enabled(true).build();

        mockMvc.perform(post("/api/users/admin/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser(username = "alireza", roles = {"ADMIN"})
    public void getUserDetailByName_get_responseEntity() throws Exception {
        when(userService.getByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/users/{username}", user.getUsername())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser(username = "alireza", roles = {"ADMIN"})
    public void updateUserDetailByName_put_responseEntity() throws Exception {
        UserDTO dto = UserDTO.builder()
                .username("alireza")
                .email("example@email.com")
                .password("password")
                .role(Set.of(role))
                .enabled(true).build();

        when(userService.updateUserDetailsByUsername(anyString(), any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/admin/{username}", user.getUsername())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    @WithMockUser(username = "alireza", roles = {"ADMIN"})
    public void updateUserProfileByHimselfWithUsername_put_responseEntity() throws Exception {
        UserProfileUpdateDTO dto = UserProfileUpdateDTO.builder()
                .username("alireza")
                .email("example@email.com")
                .password("password").build();

        when(userService.selfUpdateUserByUsername(anyString(), any(UserProfileUpdateDTO.class))).thenReturn(user);


        mockMvc.perform(put("/api/users/{username}", user.getUsername())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }


}
