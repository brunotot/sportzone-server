package com.sportzone21.server.service;

import com.sportzone21.server.exception.ApiException;
import com.sportzone21.server.model.Role;
import com.sportzone21.server.model.User;
import com.sportzone21.server.repository.UserRepository;
import com.sportzone21.server.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private static final String USER_NOT_FOUND_TITLE = "No user with given username";
    private static final String USER_NOT_FOUND_DESCRIPTION = "The user doesn't exist";
    private static final String USERNAME_EXISTS_TITLE = "Username exists";
    private static final String USERNAME_EXISTS_DESCRIPTION = "Username is already in use";
    private static final String BAD_CREDENTIALS_TITLE = "Bad credentials";
    private static final String BAD_CREDENTIALS_DESCRIPTION = "Invalid username/password supplied";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder,
                       final JwtTokenProvider jwtTokenProvider,
                       final AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public String login(final String username, final String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = findByUsername(username);
            Set<Role> roles = user.getRoles();
            return jwtTokenProvider.createToken(user, new ArrayList<>(roles));
        } catch (AuthenticationException e) {
            throw new ApiException(
                    HttpStatus.UNPROCESSABLE_ENTITY, BAD_CREDENTIALS_TITLE, BAD_CREDENTIALS_DESCRIPTION);
        }
    }

    public String register(User user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user, new ArrayList<>(user.getRoles()));
        } else {
            throw new ApiException(
                    HttpStatus.UNPROCESSABLE_ENTITY, USERNAME_EXISTS_TITLE, USERNAME_EXISTS_DESCRIPTION);
        }
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public User findByUsername(String username) {
        return Optional
                .ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, USER_NOT_FOUND_TITLE, USER_NOT_FOUND_DESCRIPTION));
    }

}
