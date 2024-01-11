package com.github.xsssecurity.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * XSS 处理程序
 *
 * @author Tao.Gan
 * @date 2023/11/29
 */
public interface XSSHandler {

    default void init(XSSHandlerConfig config) {
    }

    /**
     * 处理
     *
     * @param request 请求
     * @return HTTP Servlet 请求
     */
    HttpServletRequest handle(HttpServletRequest request);

}
