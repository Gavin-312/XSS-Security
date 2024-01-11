package com.github.xsssecurity.handler;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * XSS 处理程序映射
 *
 * @author Tao.Gan
 * @date 2023/12/04
 */
public interface XSSHandlerMapping {

    /**
     * 获取 XSS 处理程序
     *
     * @param request 请求
     * @return XSS 处理程序
     */
    @Nullable
    XSSHandler getXSSHandler(HttpServletRequest request);
}
