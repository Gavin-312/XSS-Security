package com.github.xsssecurity.handler.support;

import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.handler.XSSHandlerConfig;
import com.github.xsssecurity.handler.XSSMatcherHandler;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 表单 XSS 处理程序匹配器
 *
 * @author Tao.Gan
 * @date 2023/11/30
 */
public class FormXSSHandlerMatcher implements XSSMatcherHandler {

    private XSSCleaner cleaner;

    @Override
    public void init(XSSHandlerConfig config) {
        this.cleaner = config.getXSSCleaner();
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String contentType = request.getContentType();
        return MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)
                || StringUtils.startsWithIgnoreCase(contentType, MediaType.MULTIPART_FORM_DATA_VALUE);

    }

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        return new FormXSSHttpServletRequestWrapper(request, cleaner);
    }

    static class FormXSSHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final XSSCleaner cleaner;

        public FormXSSHttpServletRequestWrapper(HttpServletRequest request, XSSCleaner cleaner) {
            super(request);
            this.cleaner = cleaner;
        }

        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null)
                return null;
            int count = values.length;
            String[] xssCleanValues = new String[count];
            for (int i = 0; i < count; i++) {
                xssCleanValues[i] = clean(values[i]);
            }
            return xssCleanValues;
        }

        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            if (value == null)
                return null;
            return clean(value);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> paramMap = super.getParameterMap();
            if (CollectionUtils.isEmpty(paramMap)) {
                return paramMap;
            }
            Map<String, String[]> xssCleanParamMap = new HashMap<>();
            Set<Map.Entry<String, String[]>> entries = paramMap.entrySet();
            for (Map.Entry<String, String[]> entry : entries) {
                String[] values = entry.getValue();
                String[] xssCleanValue = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    xssCleanValue[i] = clean(values[i]);
                }
                xssCleanParamMap.put(entry.getKey(), xssCleanValue);
            }
            return xssCleanParamMap;
        }

        private String clean(String value) {
            return cleaner.clean(value);
        }

    }
}
