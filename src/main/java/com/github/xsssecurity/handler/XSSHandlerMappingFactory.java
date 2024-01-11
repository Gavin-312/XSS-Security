package com.github.xsssecurity.handler;

/**
 * XSS 处理程序映射
 *
 * @author Tao.Gan
 * @date 2023/12/04
 */
public interface XSSHandlerMappingFactory {

    /**
     * 创建 XSS 处理程序映射
     *
     * @return XSS 处理程序映射
     */
    XSSHandlerMapping create();
}
