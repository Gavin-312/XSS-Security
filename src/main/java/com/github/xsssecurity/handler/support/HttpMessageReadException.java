package com.github.xsssecurity.handler.support;

import com.github.xsssecurity.exception.XSSProtectionException;
import org.springframework.lang.Nullable;

/**
 * HTTP 消息读取异常
 *
 * @author Tao.Gan
 * @date 2023/12/08
 */
public class HttpMessageReadException extends XSSProtectionException {
    public HttpMessageReadException(String msg) {
        super(msg);
    }
    public HttpMessageReadException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
