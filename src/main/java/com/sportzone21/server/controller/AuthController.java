package com.sportzone21.server.controller;

import com.sportzone21.server.form.LoginForm;
import com.sportzone21.server.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;

    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public String login(@RequestBody @Valid LoginForm form) {
        String username = form.getUsername();
        String password = form.getPassword();
        return userService.login(username, password);
    }

}
