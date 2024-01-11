package com.github.xsssecurity.exception;

import org.springframework.lang.Nullable;

/**
 * XSS 保护异常
 *
 * @author Tao.Gan
 * @date 2023/12/08
 */
public abstract class XSSProtectionException extends RuntimeException {
    public XSSProtectionException(String msg) {
        super(msg);
    }

    public XSSProtectionException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
