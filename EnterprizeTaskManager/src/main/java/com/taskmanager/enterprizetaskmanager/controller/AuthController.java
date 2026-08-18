package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.InfoDTO;
import com.taskmanager.enterprizetaskmanager.dto.UserDTO;
import com.taskmanager.enterprizetaskmanager.security.jwt.JwtToken;
import com.taskmanager.enterprizetaskmanager.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtToken jwtToken;
    private UserServiceImpl userService;

    public AuthController(AuthenticationManager authenticationManager, JwtToken jwtToken) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid() @RequestBody InfoDTO info) {
        var pocket = new UsernamePasswordAuthenticationToken(info.getUsernameOrEmail(), info.getPassword());
        var auth = authenticationManager.authenticate(pocket);

//        List<String> authorities = new ArrayList<>();
//        for (var a : auth.getAuthorities()) {
//            authorities.add(a.getAuthority());
//        }

        //TODO: gotta check this
        List<String> authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList();

        return ResponseEntity.ok(jwtToken.tokenMaker(info.getUsernameOrEmail(), authorities));

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {
        var addedUSer = userService.addUser(userDTO);
//        List<String> authorities = new ArrayList<>();
//        for (var a : addedUSer.getAuthorities()) {
//            authorities.add(a.getAuthority());
//        }

        //TODO: gotta check this
        List<String> authorities = addedUSer.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList();
        return ResponseEntity.ok(jwtToken.tokenMaker(addedUSer.getUsername(), authorities));
    }


}
