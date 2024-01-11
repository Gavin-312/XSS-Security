package com.github.xsssecurity.handler.support;


import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.handler.XSSMatcherHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * HTTP 请求头 XSS 处理程序匹配器
 *
 * @author Tao.Gan
 * @date 2023/11/30
 */
public class HeaderXSSHandlerMatcher implements XSSMatcherHandler {

    private final XSSCleaner cleaner;

    public HeaderXSSHandlerMatcher(XSSCleaner cleaner) {
        this.cleaner = cleaner;
    }

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        return new HeaderXSSHttpServletRequestWrapper(request, cleaner);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    static class HeaderXSSHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final XSSCleaner cleaner;

        public HeaderXSSHttpServletRequestWrapper(HttpServletRequest request, XSSCleaner cleaner) {
            super(request);
            this.cleaner = cleaner;
        }

        @Override
        public Cookie[] getCookies() {
            Cookie[] cookies = super.getCookies();
            if (cookies == null) {
                return null;
            }
            Cookie[] xssCleanCookies = new Cookie[cookies.length];
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null) {
                    cookie = new Cookie(cookie.getName(), cleaner.clean(cookie.getValue()));
                }
                xssCleanCookies[i] = cookie;
            }
            return xssCleanCookies;
        }

        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null)
                return null;
            return cleaner.clean(value);
        }
    }
}
