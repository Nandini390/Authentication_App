package org.example.authapp.controllers;

import lombok.RequiredArgsConstructor;
import org.example.authapp.Dtos.LoginRequest;
import org.example.authapp.Dtos.TokenResponse;
import org.example.authapp.Dtos.UserDto;
import org.example.authapp.Repositories.UserRepository;
import org.example.authapp.Services.AuthService;
import org.example.authapp.entities.User;
import org.example.authapp.security.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) throws IllegalAccessException {
         UserDto response=authService.registerUser(userDto);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){
           Authentication authentication=authenticate(loginRequest);
           User user=userRepository.findByEmail(loginRequest.email()).orElseThrow(()-> new BadCredentialsException("Invalid Username or Password"));
           if(!user.isEnabled()){
               throw new DisabledException("user is disabled");
           }
           String accessToken= jwtService.generateAccessToken(user);
           TokenResponse tokenResponse=TokenResponse.of(accessToken,"", jwtService.getAccessTtlSeconds(), modelMapper.map(user,UserDto.class));
           return ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try{
           return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
        }catch(Exception e){
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
