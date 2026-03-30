package org.example.authapp.Services.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.authapp.Dtos.UserDto;
import org.example.authapp.Repositories.UserRepository;
import org.example.authapp.Services.UserService;
import org.example.authapp.entities.Provider;
import org.example.authapp.entities.User;
import org.example.authapp.exception.ResourceNotFoundException;
import org.example.authapp.exception.UserAlreadyExistsException;
import org.example.authapp.helpers.UserHelper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto){
        if(userDto.getEmail()==null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new UserAlreadyExistsException("Email already Exists");
        }
        //TODO:assign role here to new user____ for authorization
        User user=modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider()!=null? userDto.getProvider(): Provider.LOCAL);
        User savedUser=userRepository.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user not found with given emailId"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId=UserHelper.parseUUID(userId);
        User existingUser=userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));
        if(userDto.getName()!=null) existingUser.setName(userDto.getName());
        if(userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());
        if(userDto.getGender()!=null) existingUser.setGender(userDto.getGender());
        //TODO:change password updation logic
        if(userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.getEnable());
        User updatedUser=userRepository.save(existingUser);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
       UUID uId = UserHelper.parseUUID(userId);
       User user=userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));
       userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(()->new ResourceNotFoundException("user with id not found"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user , UserDto.class))
                .toList();
    }
}
