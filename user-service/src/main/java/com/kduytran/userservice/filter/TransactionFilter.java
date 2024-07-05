package com.kduytran.userservice.filter;

import com.kduytran.userservice.utils.TransactionIdHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TransactionFilter implements Filter {

    private final TransactionIdHolder transactionIdHolder;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String transactionId = getTransactionId(req);
        if (transactionId != null) {
            transactionIdHolder.setCurrentTransactionId(transactionId);
            res.addHeader(TransactionIdHolder.HEADER_NAME, transactionId);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTransactionId(HttpServletRequest req) {
        return req.getHeader(TransactionIdHolder.HEADER_NAME);
    }

}
