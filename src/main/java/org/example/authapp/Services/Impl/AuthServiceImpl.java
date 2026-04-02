package org.example.authapp.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.authapp.Dtos.UserDto;
import org.example.authapp.Services.AuthService;
import org.example.authapp.Services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) throws IllegalAccessException {
        //verify email
        //verify password
        //default roles
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setEnable(true);
        return userService.createUser(userDto);
    }
}
