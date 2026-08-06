package com.openmind.order.shared;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Equivalent of a custom ASP.NET Core logging middleware (app.Use(...)): runs
 * once per request, outside the Spring MVC dispatcher, so it logs every request
 * -- including ones that never match a controller (404s).
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter
{
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        long startTime = System.currentTimeMillis();
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            long durationMs = System.currentTimeMillis() - startTime;
            log.info("{} {} -> {} ({} ms)", request.getMethod(), request.getRequestURI(), response.getStatus(),
                    durationMs);
        }
    }
}
