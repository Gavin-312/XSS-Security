package com.github.xsssecurity;

import com.github.xsssecurity.handler.XSSHandler;
import com.github.xsssecurity.handler.XSSHandlerMapping;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS 安全防护 Servlet 过滤器
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class XSSServletFilter extends OncePerRequestFilter {
    private final XSSHandlerMapping xssHandlerMapping;

    public XSSServletFilter(XSSHandlerMapping xssHandlerMapping) {
        Assert.notNull(xssHandlerMapping, "XSSHandlerMapping must not be null.");
        this.xssHandlerMapping = xssHandlerMapping;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest xssCleanerRequest = null;
        XSSHandler xssHandler = xssHandlerMapping.getXSSHandler(request);
        if (xssHandler != null) {
            xssCleanerRequest = xssHandler.handle(request);
        }
        if (xssCleanerRequest == null) {
            xssCleanerRequest = request;
        }
        filterChain.doFilter(xssCleanerRequest, response);
    }

}
