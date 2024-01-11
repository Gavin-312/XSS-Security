package com.github.xsssecurity.config;

/**
 * XSS 配置工厂
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public interface XSSConfigFactory {

    /**
     * 获取 XSS 配置
     *
     * @return XSS 配置
     */
    XSSConfig getXSSConfig();
}
