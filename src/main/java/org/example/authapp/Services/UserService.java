package org.example.authapp.Services;

import org.example.authapp.Dtos.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto) throws IllegalAccessException;
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto, String userId);
    void deleteUser(String userId);
    UserDto getUserById(String userId);
    List<UserDto> getAllUsers();
}
