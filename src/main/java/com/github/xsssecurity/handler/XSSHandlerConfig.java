package com.github.xsssecurity.handler;

import com.github.xsssecurity.cleaner.XSSCleaner;
import org.springframework.lang.Nullable;

import java.util.Collection;

/**
 * XSS 处理程序配置
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public interface XSSHandlerConfig {

    /**
     * XSS 处理程序名称
     *
     * @return XSSHandler 名称
     */
    String getXSSHandlerName();

    /**
     * 获取初始化参数
     *
     * @param name 名字
     * @return 参数
     */
    @Nullable
    String getInitParameter(String name);

    /**
     * 获取初始化参数名称
     *
     * @return 初始化参数名枚举
     */
    Collection<String> getInitParameterNames();

    /**
     * 获取XSS恶意代码清理策略
     *
     * @return XSS恶意代码清理策略
     */
    XSSCleaner getXSSCleaner();


}
