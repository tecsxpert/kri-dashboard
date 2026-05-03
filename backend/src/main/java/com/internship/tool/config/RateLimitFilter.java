package com.internship.tool.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Filter using Bucket4j.
 * Day 13 — Rate Limiting & API Protection
 *
 * Rules:
 *  - Auth endpoints (/api/v1/auth/**): 10 requests / minute per IP
 *  - All other endpoints: 60 requests / minute per IP
 */
@Component
@Slf4j
public class RateLimitFilter implements Filter {

    /** One bucket per client IP address */
    private final Map<String, Bucket> authBuckets    = new ConcurrentHashMap<>();
    private final Map<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ip  = getClientIp(req);
        String uri = req.getRequestURI();

        Bucket bucket = uri.startsWith("/api/v1/auth")
                ? authBuckets   .computeIfAbsent(ip, k -> buildAuthBucket())
                : generalBuckets.computeIfAbsent(ip, k -> buildGeneralBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            log.warn("[RATE_LIMIT] Blocked IP={} URI={}", ip, uri);
            resp.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            resp.setContentType("application/json");
            resp.getWriter().write(
                    "{\"status\":429,\"message\":\"Too many requests — please slow down.\",\"path\":\"" + uri + "\"}"
            );
        }
    }

    // ── Bucket factories ──────────────────────────────────────────────────────

    /** 10 requests per 60 seconds for auth endpoints */
    private Bucket buildAuthBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(10)
                        .refillGreedy(10, Duration.ofMinutes(1))
                        .build())
                .build();
    }

    /** 60 requests per 60 seconds for general endpoints */
    private Bucket buildGeneralBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(60)
                        .refillGreedy(60, Duration.ofMinutes(1))
                        .build())
                .build();
    }

    private String getClientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        return (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : req.getRemoteAddr();
    }
}
