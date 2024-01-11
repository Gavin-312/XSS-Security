package com.github.xsssecurity.cleaner;

import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * XSS恶意代码清理策略
 *
 * @author Tao.Gan
 * @date 2023/11/29
 */
public interface XSSCleaner {

    /**
     * 清理 html
     *
     * @param bodyHtml 要清理的HTML
     * @return 清理后的数据
     */
    @Nullable
    String clean(@Nullable String bodyHtml);

    /**
     * 设置属性
     *
     * @param properties 属性
     */
    default void setProperties(@Nullable Properties properties) {
    }
}