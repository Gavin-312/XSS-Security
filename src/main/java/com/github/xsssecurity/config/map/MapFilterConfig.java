package com.github.xsssecurity.config.map;

import com.github.xsssecurity.config.CleanerConfig;
import com.github.xsssecurity.config.FilterConfig;
import com.github.xsssecurity.config.XSSConfig;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于映射类型的过滤器配置
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class MapFilterConfig implements FilterConfig {
    static final String XSS_FILTER_NAME = "filter-name";
    static final String XSS_FILTER_CLASS = "filter-class";
    static final String XSS_FILTER_URL_PATTERNS = "url-patterns";
    static final String XSS_FILTER_INIT_PARAM = "init-param";
    static final String XSS_FILTER_CLEANER_REF = "cleaner-ref";
    static final String XSS_FILTER_CLEANER = "cleaner";
    private final XSSConfig xssConfig;
    private String filterName;
    @Nullable
    private String filterClass;
    private List<String> urlPatterns;
    @Nullable
    private Map<String, String> initParameters;
    @Nullable
    private CleanerConfig cleanerConfig;

    public MapFilterConfig(Map<String, Object> filter, XSSConfig xssConfig) {
        this.xssConfig = xssConfig;
        if (!CollectionUtils.isEmpty(filter)) {
            Assert.notNull(xssConfig, "XSSConfig cannot be null.");
            load(filter);
        }
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    @Nullable
    public String getFilterClass() {
        return filterClass;
    }

    @Override
    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    @Override
    @Nullable
    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    @Override
    @Nullable
    public CleanerConfig getCleanerConfig() {
        return cleanerConfig;
    }

    @SuppressWarnings("unchecked")
    protected void load(Map<String, Object> filter) {
        Object object = filter.get(XSS_FILTER_NAME);
        Assert.notNull(object, "filter name cannot be null.");
        this.filterName = object.toString();

        object = filter.get(XSS_FILTER_CLASS);
        if (object != null) {
            this.filterClass = object.toString();
        }

        object = filter.get(XSS_FILTER_URL_PATTERNS);
        Assert.notNull(object, filterName + " url patterns cannot be null.");
        if (object instanceof String) {
            this.urlPatterns = Arrays.stream(object.toString().split(","))
                    .map(String::trim).collect(Collectors.toList());
        } else if (object instanceof List) {
            List<String> list = (List<String>) object;
            Assert.notEmpty(list, filterName + " url patterns cannot be empty.");
            this.urlPatterns = Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(filterName + " url patterns must be a String or a List<String>.");
        }

        object = filter.get(XSS_FILTER_INIT_PARAM);
        if (object instanceof Map) {
            Map<String, String> properties = new HashMap<>();
            Map<String, Object> initParamMap = (Map<String, Object>) object;
            if (!CollectionUtils.isEmpty(initParamMap)) {
                for (Map.Entry<String, Object> en : initParamMap.entrySet()) {
                    properties.put(en.getKey(), Objects.toString(en.getValue(), null));
                }
            }
            this.initParameters = Collections.unmodifiableMap(properties);
        }

        object = filter.get(XSS_FILTER_CLEANER);
        if (object instanceof Map) {
            this.cleanerConfig = new MapCleanerConfig((Map<String, Object>) object);
        } else {
            object = filter.get(XSS_FILTER_CLEANER_REF);
            if (object != null) {
                this.cleanerConfig = xssConfig.getCleanerConfig(object.toString());
            }
        }

    }
}
