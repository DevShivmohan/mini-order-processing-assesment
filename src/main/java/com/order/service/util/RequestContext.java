package com.order.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.order.service.model.LoggedInUserDatail;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class RequestContext {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static LoggedInUserDatail getUserFromRequestContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoggedInUserDatail loggedInUserDatail) {
            return loggedInUserDatail;
        }
        return LoggedInUserDatail.builder().build();
    }

    public static String convertObjectToJsonString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error during write value as string ", e);
            return null;
        }
    }

    public static <TARGET_TYPE> TARGET_TYPE convertJsonStringToObject(String json, Class<TARGET_TYPE> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            log.error("Error during read value as object ", e);
            return null;
        }
    }
}
