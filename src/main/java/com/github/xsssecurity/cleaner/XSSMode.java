package com.github.xsssecurity.cleaner;

/**
 * XSS 清理模式, 参考{@link ModeXSSCleaner}
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public enum XSSMode {
    /**
     * 过滤
     */
    FILTER,
    /**
     * 转义
     */
    ESCAPE,
}