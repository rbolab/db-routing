package com.mycompany.myapp.web.filter;

import com.mycompany.myapp.config.context.DataSourceContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ContextualDataSourceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String context = request.getHeader("Context");
        DataSourceContext.setCurrentContext(context);
        filterChain.doFilter(request, response);
    }
}
