package com.lcwd.electronic.store.dtos;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private UserDto user;
}
