package com.github.xsssecurity.handler.support;

import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.handler.XSSHandlerConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * Http 请求消息 XSS 处理程序
 *
 * @author Tao.Gan
 * @date 2023/11/29
 */
public class HttpRequestMessageXSSHandler extends CompositeXSSMatcherHandler {
    private HeaderXSSHandlerMatcher headerXSSHandlerMatcher;

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        HttpServletRequest handledRequest = headerXSSHandlerMatcher.handle(request);
        return super.handle(handledRequest);
    }

    @Override
    public void init(XSSHandlerConfig config) {
        XSSCleaner xssCleaner = config.getXSSCleaner();
        this.headerXSSHandlerMatcher = new HeaderXSSHandlerMatcher(xssCleaner);

        JsonXSSHandler jsonXSSHandler = new JsonXSSHandler();
        jsonXSSHandler.init(config);
        addXSSHandler(jsonXSSHandler);

        FormXSSHandlerMatcher formXSSHandlerMatcher = new FormXSSHandlerMatcher();
        formXSSHandlerMatcher.init(config);
        addXSSHandler(formXSSHandlerMatcher);
    }
}
