package com.github.xsssecurity.cleaner;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Properties;

/**
 * XSS 恶意代码清理模式
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class ModeXSSCleaner implements XSSCleaner {

    private XSSMode mode = XSSMode.FILTER;
    /**
     * 是否开启转义, 当 XSSMode 为 FILTER 时才生效
     */
    private boolean enableEscape = true;

    @Override
    public String clean(String bodyHtml) {
        // 1. 为空直接返回
        if (!StringUtils.hasText(bodyHtml)) {
            return bodyHtml;
        }
        if (XSSMode.ESCAPE == mode) {
            // html 转义
            return HtmlUtils.htmlEscape(bodyHtml);
        } else {
            String filter = new HTMLFilter().filter(bodyHtml);
            if (enableEscape) {
                return filter;
            }
            // html 反转义
            return HtmlUtils.htmlUnescape(filter);
        }
    }

    public void setMode(XSSMode mode) {
        this.mode = mode;
    }

    public void setEnableEscape(boolean enableEscape) {
        this.enableEscape = enableEscape;
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties != null) {
            setMode(XSSMode.valueOf(
                    properties.getProperty("mode", "filter").toUpperCase()));
            setEnableEscape(Boolean.parseBoolean(properties.getProperty("enableEscape")));
        }

    }
}
