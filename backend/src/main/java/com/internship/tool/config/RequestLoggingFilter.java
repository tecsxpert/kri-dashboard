package com.internship.tool.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Request / Response logging filter.
 * Day 15 — Performance Logging & Final Polish
 *
 * Logs:
 *   ▶ method, URI, client IP on request entry
 *   ✔ HTTP status and elapsed time on response
 */
@Component
@Order(1)
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Skip logging for actuator and swagger calls to reduce noise
        String uri = req.getRequestURI();
        if (uri.startsWith("/actuator") || uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        long   start  = System.currentTimeMillis();
        String method = req.getMethod();
        String ip     = getClientIp(req);

        log.info("[REQUEST]  ▶ {} {} from {}", method, uri, ip);

        try {
            chain.doFilter(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info("[RESPONSE] ✔ {} {} → {} | {}ms", method, uri, resp.getStatus(), elapsed);
        }
    }

    private String getClientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        return (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : req.getRemoteAddr();
    }
}
