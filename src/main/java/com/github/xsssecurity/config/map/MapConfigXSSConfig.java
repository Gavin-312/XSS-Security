package com.github.xsssecurity.config.map;

import com.github.xsssecurity.config.CleanerConfig;
import com.github.xsssecurity.config.FilterConfig;
import com.github.xsssecurity.config.XSSConfig;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 基于映射类型的XSS安全防护配置
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class MapConfigXSSConfig implements XSSConfig {
    private static final String XSS = "xss";
    private static final String XSS_ENABLED = "enabled";
    private static final String XSS_CLEANERS = "cleaner";
    private static final String XSS_FILTERS = "filter";

    private boolean enabled;
    private final Map<String, CleanerConfig> cleanerConfigMap = new HashMap<>();
    private final Map<String, FilterConfig> filtercleanerConfigMap = new HashMap<>();

    public MapConfigXSSConfig(Map<String, Object> mapConfig) {
        if (!CollectionUtils.isEmpty(mapConfig)) {
            load(mapConfig);
        }
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public Enumeration<String> getCleanerNames() {
        return Collections.enumeration(cleanerConfigMap.keySet());
    }

    @Override
    public Enumeration<String> getFilterNames() {
        return Collections.enumeration(filtercleanerConfigMap.keySet());
    }

    @Override
    public CleanerConfig getCleanerConfig(String cleanerName) {
        return cleanerConfigMap.get(cleanerName);
    }

    @Override
    public FilterConfig getFilterConfig(String filterName) {
        return filtercleanerConfigMap.get(filterName);
    }

    /**
     * 加载配置
     *
     * @param mapConfig Map配置
     */
    public void load(Map<String, Object> mapConfig) {
        Object object = mapConfig.get(XSS);
        if (!(object instanceof Map)) {
            throw new IllegalArgumentException("xss must be a Map");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> xssConfig = (Map<String, Object>) object;

        enabled = Boolean.parseBoolean(Objects.toString(xssConfig.get(XSS_ENABLED)));

        Object cleanersValue = xssConfig.get(XSS_CLEANERS);
        if (cleanersValue instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cleaners = (List<Map<String, Object>>) cleanersValue;
            cleaners.forEach(e -> {
                CleanerConfig config = this.loadCleaner(e);
                cleanerConfigMap.put(config.getCleanerName(), config);
            });
        }

        Object filtersValue = xssConfig.get(XSS_FILTERS);
        if (filtersValue instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cleaners = (List<Map<String, Object>>) filtersValue;
            cleaners.forEach(e -> {
                FilterConfig config = this.loadFilter(e);
                filtercleanerConfigMap.put(config.getFilterName(), config);
            });
        }

    }

    protected CleanerConfig loadCleaner(Map<String, Object> cleaner) {
        return new MapCleanerConfig(cleaner);
    }

    protected FilterConfig loadFilter(Map<String, Object> filter) {
        return new MapFilterConfig(filter, this);

    }

}
