package com.github.xsssecurity.handler.support;

import com.github.xsssecurity.handler.XSSHandler;
import com.github.xsssecurity.handler.XSSMatcherHandler;
import com.github.xsssecurity.handler.XSSMatcher;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * XSS 复合处理程序
 *
 * @author Tao.Gan
 * @date 2023/11/29
 */
public class CompositeXSSHandler implements XSSHandler {
    private final List<XSSMatcherHandler> handlers = new ArrayList<>();

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        XSSHandler xssHandler = getXssHandler(request);
        if (xssHandler == null) {
            return request;
        }
        return xssHandler.handle(request);
    }

    @Nullable
    protected XSSHandler getXssHandler(HttpServletRequest request) {
        for (XSSMatcherHandler handler : handlers) {
            if (handler.matches(request)) {
                return handler;
            }
        }
        return null;
    }

    public void addXSSHandler(XSSHandler xssHandler) {
        this.addXSSMatcherHandler(XSSHandlerConvertMatcher.convert(xssHandler), xssHandler);
    }

    public void addXSSMatcherHandler(XSSMatcher matcher, XSSHandler handler) {
        Assert.notNull(matcher, "XSSMatcher not be null.");
        Assert.notNull(handler, "XSSHandler not be null.");
        this.addXSSMatcherHandler(new XSSMatcherHandlerPair(matcher, handler));
    }

    public void addXSSMatcherHandler(XSSMatcherHandler handlerMatcher) {
        Assert.notNull(handlerMatcher, "XSSMatcherHandler not be null.");
        this.handlers.add(handlerMatcher);
    }

}
