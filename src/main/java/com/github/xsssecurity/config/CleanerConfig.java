package com.github.xsssecurity.config;

import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * XSS恶意代码清理策略配置
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public interface CleanerConfig {

    /**
     * 获取清理策略名称
     *
     * @return 清理策略名称
     */
    String getCleanerName();

    /**
     * 获得清理策略类型
     *
     * @return 清理策略类型
     */
    @Nullable
    String getCleanerClass();

    /**
     * 获取自定义属性
     *
     * @return 自定义属性
     */
    @Nullable
    Properties getProperty();

}
