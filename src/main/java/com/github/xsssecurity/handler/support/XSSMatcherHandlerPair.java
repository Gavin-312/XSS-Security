package com.github.xsssecurity.handler.support;


import com.github.xsssecurity.handler.XSSHandler;
import com.github.xsssecurity.handler.XSSMatcherHandler;
import com.github.xsssecurity.handler.XSSMatcher;

import javax.servlet.http.HttpServletRequest;

public class XSSMatcherHandlerPair implements XSSMatcherHandler {
    private final XSSMatcher xssMatcher;
    private final XSSHandler xssHandler;

    public XSSMatcherHandlerPair(XSSMatcher xssMatcher, XSSHandler xssHandler) {
        this.xssMatcher = xssMatcher;
        this.xssHandler = xssHandler;
    }

    public XSSMatcher getXssMatcher() {
        return xssMatcher;
    }

    public XSSHandler getXssHandler() {
        return xssHandler;
    }

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        return xssHandler.handle(request);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return xssMatcher.matches(request);
    }
}

