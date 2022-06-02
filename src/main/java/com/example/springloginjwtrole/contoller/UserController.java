package com.example.springloginjwtrole.contoller;

import com.example.springloginjwtrole.model.AuthenticationToken;
import com.example.springloginjwtrole.model.LoginUser;
import com.example.springloginjwtrole.model.User;
import com.example.springloginjwtrole.model.UserDto;
import com.example.springloginjwtrole.service.UserService;
import com.example.springloginjwtrole.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenUtil jwtTokenUtil;
    @Autowired
    UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getName(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);;
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationToken(token));
    }

    @PostMapping("/register")
    public User saveUser(@RequestBody UserDto user) {
        return userService.save(user);
    }

    // @PreAuthorize("hasAuthority('ADMIN')")
    @RolesAllowed("ADMIN")
    @GetMapping("/adminping")
    public String adminPing() {
        return "Only Admins can read this";
    }

    // @PreAuthorize("hasAuthority('USER')")
    @RolesAllowed("USER")
    @GetMapping("/userping")
    public String userPing() {
        return "Any User can read this";
    }
}
