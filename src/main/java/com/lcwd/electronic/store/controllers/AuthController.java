package com.lcwd.electronic.store.controllers;


import com.lcwd.electronic.store.dtos.*;

import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.security.JwtToken;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final ModelMapper modelMapper;

    private final JwtToken jwtToken;

    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {

            //created authentication
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            //authenticating
            authenticationManager.authenticate(authentication);

            //getting userdetail
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            UserDto userDto = modelMapper.map(userRepository.findByEmail(userDetails.getUsername()).get(), UserDto.class);
            //getting token
            String token = jwtToken.generateToken(userDto.getEmail(), true);
            String refreshToken = jwtToken.generateToken(userDto.getEmail(), false);
            JwtResponse build = JwtResponse
                    .builder()
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .user(userDto)
                    .build();
            return ResponseEntity.ok(build);

        } catch (Exception ex) {
            // 6. Handle invalid credentials
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .errors(List.of("Invalid Username or Password"))
                            .success(false)
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }
    }


    //refresh token api

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequeest
    ) {


        //refresh token ko valildate
        if (jwtToken.validateToken(refreshTokenRequeest.getRefreshToken()) && jwtToken.isRefreshToken(refreshTokenRequeest.getRefreshToken())) {

            //refresh token se username nikal rehe
            String usernameFromRefreshToken = jwtToken.extractEmail(refreshTokenRequeest.getRefreshToken());
            //user data fetch kiya hia from database
            UserDto userDto = modelMapper.map(userRepository.findByEmail(usernameFromRefreshToken).get(), UserDto.class);

            //new access token generate kiya hai.
            String accessToken = jwtToken.generateToken(userDto.getEmail(), true);
            //new refresh token
            String newRefreshToken = jwtToken.generateToken(userDto.getEmail(), false);
            JwtResponse response = JwtResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .user(userDto).build();

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .errors(List.of("Invalid Username or Password"))
                            .timestamp(LocalDateTime.now())
                            .success(false)
                            .build()
            );
        }


    }


}
