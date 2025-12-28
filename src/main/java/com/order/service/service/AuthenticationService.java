package com.order.service.service;

import com.order.service.dto.AuthReqDto;
import com.order.service.dto.TokenResDto;
import com.order.service.exception.GenericException;
import com.order.service.util.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResDto login(AuthReqDto authReqDto) {
        final var user = userService.getByUsername(authReqDto.getUsername());
        if (!userService.matches(authReqDto.getPassword(), user.getPassword())) {
            throw new GenericException(HttpStatus.FORBIDDEN.value(), "Incorrect username password");
        }
        return jwtTokenProvider.createToken(user.getId());
    }
}
