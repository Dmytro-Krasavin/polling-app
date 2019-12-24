package com.example.polls.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class IpAddressExtractor {

    private final HttpServletRequest request;

    public String getClientIP() {
        String xForwardedHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedHeader == null) {
            return request.getRemoteAddr();
        }
        return xForwardedHeader.split(",")[0];
    }
}
