package com.kduytran.userservice.filter;

import com.kduytran.userservice.constant.RequestConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String correlationId = httpRequest.getHeader(RequestConstant.CORRELATION_ID);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        try {
            MDC.put(RequestConstant.CORRELATION_ID, correlationId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            httpResponse.setHeader(RequestConstant.CORRELATION_ID, correlationId);
            MDC.remove(RequestConstant.CORRELATION_ID);
        }
    }

}
