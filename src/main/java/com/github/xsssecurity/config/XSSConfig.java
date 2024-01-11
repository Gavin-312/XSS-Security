package com.github.xsssecurity.config;


import org.springframework.lang.Nullable;

import java.util.Enumeration;

/**
 * XSS 安全防护配置
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public interface XSSConfig {

    /**
     * 是否启用XSS安全防护
     *
     * @return true为启用
     */
    boolean enabled();

    /**
     * 获取清理策略名称列举
     *
     * @return 清理策略名称列举
     */
    Enumeration<String> getCleanerNames();

    /**
     * 获取过滤器名称列举
     *
     * @return 过滤器名称列举
     */
    Enumeration<String> getFilterNames();

    /**
     * 获取清理策略配置
     *
     * @param cleanerName 清理策略配置名称
     * @return 清理策略配置
     */
    @Nullable
    CleanerConfig getCleanerConfig(String cleanerName);

    /**
     * 获取过滤器配置
     *
     * @param filterName 过滤器名称
     * @return 过滤器配置
     */
    @Nullable
    FilterConfig getFilterConfig(String filterName);
}
