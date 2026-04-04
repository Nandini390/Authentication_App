package org.example.authapp.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.authapp.Dtos.UserDto;
import org.example.authapp.Services.AuthService;
import org.example.authapp.Services.UserService;
import org.example.authapp.config.AppConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) throws IllegalAccessException {
        //verify email
        //verify password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createUser(userDto);
    }
}
