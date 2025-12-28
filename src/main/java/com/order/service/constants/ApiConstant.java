package com.order.service.constants;

public class ApiConstant {
    public static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_TYPE = "token_type";
    public static final String TOKEN_ID = "token_id";
    public static final String USER_ID = "user_id";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String TOKEN_ISSUER = "Mini order processor";
}
