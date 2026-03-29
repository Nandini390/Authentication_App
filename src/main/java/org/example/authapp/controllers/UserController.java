package org.example.authapp.controllers;

import lombok.RequiredArgsConstructor;
import org.example.authapp.Dtos.UserDto;
import org.example.authapp.Services.UserService;
import org.example.authapp.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws Exception {
        UserDto response = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
       UserDto user=userService.getUserById(userId);
       return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/email/{e}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("e") String email){
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
      userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId,@RequestBody UserDto userDto){
      UserDto response=userService.updateUser(userDto,userId);
      return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
