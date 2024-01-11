package com.github.xsssecurity.config.map;

import com.github.xsssecurity.config.CleanerConfig;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 基于映射类型的清理策略配置
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class MapCleanerConfig implements CleanerConfig {
    static final String XSS_CLEANER_NAME = "cleaner-name";
    static final String XSS_CLEANER_CLASS = "cleaner-class";
    static final String XSS_CLEANER_PROPERTY = "property";
    private String cleanerName;
    @Nullable
    private String cleanerClass;
    @Nullable
    private Properties property;

    public MapCleanerConfig(Map<String, Object> cleaner) {
        if (!CollectionUtils.isEmpty(cleaner)) {
            load(cleaner);
        }
    }

    @Override
    public String getCleanerName() {
        return cleanerName;
    }

    @Override
    @Nullable
    public String getCleanerClass() {
        return cleanerClass;
    }

    @Nullable
    @Override
    public Properties getProperty() {
        return property;
    }

    @SuppressWarnings("unchecked")
    public void load(Map<String, Object> cleaner) {
        Object object = cleaner.get(XSS_CLEANER_NAME);
        Assert.notNull(object, "cleaner name cannot be null.");
        this.cleanerName = object.toString();

        object = cleaner.get(XSS_CLEANER_CLASS);
        if(object != null){
            this.cleanerClass = object.toString();
        }

        object = cleaner.get(XSS_CLEANER_PROPERTY);
        if (object instanceof Map) {
            Properties properties = new Properties();
            Map<String, Object> map = (Map<String, Object>) object;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                properties.setProperty(entry.getKey(),
                        Objects.toString(entry.getValue(), ""));
            }
            this.property = properties;
        }
    }
}
