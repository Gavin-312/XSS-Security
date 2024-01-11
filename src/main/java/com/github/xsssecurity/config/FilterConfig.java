package com.github.xsssecurity.config;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 过滤器配置
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public interface FilterConfig {
    /**
     * 获取过滤器名称
     *
     * @return 过滤器名称
     */
    String getFilterName();

    /**
     * 获取过滤器类型
     *
     * @return 过滤器类型
     */
    @Nullable
    String getFilterClass();

    /**
     * 获取过滤器匹配 URL
     *
     * @return 过滤器匹配 URL
     */
    List<String> getUrlPatterns();

    /**
     * 获取初始化参数映射
     *
     * @return 初始化参数映射
     */
    @Nullable
    Map<String, String> getInitParameters();

    /**
     * 获取清理策略配置
     *
     * @return 清理策略配置
     */
    @Nullable
    CleanerConfig getCleanerConfig();
}
