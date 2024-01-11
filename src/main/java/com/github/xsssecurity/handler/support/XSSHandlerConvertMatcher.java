package com.github.xsssecurity.handler.support;


import com.github.xsssecurity.handler.XSSHandler;
import com.github.xsssecurity.handler.XSSMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * XSS 处理程序包装匹配器
 *
 * @author Tao.Gan
 * @date 2023/12/06
 */
public class XSSHandlerConvertMatcher implements XSSMatcher {
    private final XSSMatcher xssMatcher;

    private XSSHandlerConvertMatcher(XSSMatcher xssMatcher) {
        this.xssMatcher = xssMatcher;
    }

    public static XSSHandlerConvertMatcher convert(XSSHandler xssHandler) {
        XSSMatcher matcher = (xssHandler instanceof XSSMatcher ? (XSSMatcher) xssHandler : r -> true);
        return new XSSHandlerConvertMatcher(matcher);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return xssMatcher.matches(request);
    }
}
