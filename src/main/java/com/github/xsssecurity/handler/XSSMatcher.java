package com.github.xsssecurity.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * XSS 匹配器
 *
 * @author Tao.Gan
 * @date 2023/11/30
 */
public interface XSSMatcher {

    /**
     * 匹配
     *
     * @param request 请求
     * @return true表示匹配，false表示不匹配
     */
    boolean matches(HttpServletRequest request);
}
