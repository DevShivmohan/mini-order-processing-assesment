package com.order.service.filter;

import com.order.service.constants.ApiConstant;
import com.order.service.model.LoggedInUserDatail;
import com.order.service.model.User;
import com.order.service.service.UserService;
import com.order.service.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(ApiConstant.AUTHORIZATION);
        if (authorization != null && !authorization.isBlank()) {
            authorization = authorization.substring(7);
        }
        if (Boolean.TRUE.equals(authorization != null && !authorization.isBlank() && jwtTokenProvider.validateToken(authorization)) && Boolean.TRUE.equals(jwtTokenProvider.isAccessToken(authorization))) {
            final User user = userService.getById(jwtTokenProvider.getUserId(authorization));
            LoggedInUserDatail loggedInUserDatail = LoggedInUserDatail.builder().userId(user.getId()).role(user.getRole()).token(authorization).build();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
            Authentication authentication = new UsernamePasswordAuthenticationToken(loggedInUserDatail, null, Collections.singletonList(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
