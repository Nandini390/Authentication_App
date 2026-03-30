package org.example.authapp.Services;

import org.example.authapp.Dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto) throws IllegalAccessException;
}
