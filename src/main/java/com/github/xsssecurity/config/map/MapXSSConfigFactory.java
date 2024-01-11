package com.github.xsssecurity.config.map;

import com.github.xsssecurity.config.XSSConfig;
import com.github.xsssecurity.config.XSSConfigFactory;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 基于映射类型的 XSS 配置工厂
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public abstract class MapXSSConfigFactory implements XSSConfigFactory {

    @Override
    public XSSConfig getXSSConfig() {
        Map<String, Object> mapConfig = getMapConfig();
        Assert.notNull(mapConfig, "MapConfig argument cannot be null.");
        return new MapConfigXSSConfig(mapConfig);
    }

    /**
     * 获取 XSS 配置
     *
     * @return XSS 配置
     */
    protected abstract Map<String, Object> getMapConfig();
}
