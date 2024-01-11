package com.github.xsssecurity.config.map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Yaml 配置 XSS 处理程序工厂
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public class YamlXSSConfigFactory extends MapXSSConfigFactory {

    @Nullable
    private Yaml yaml;
    private final String classpathResource;

    public YamlXSSConfigFactory(String classpathResource) {
        Assert.hasText(classpathResource, "Resource cannot be empty.");
        this.classpathResource = classpathResource;
    }

    @Override
    protected Map<String, Object> getMapConfig() {
        Map<String, Object> config = createMapConfig();
        Assert.notNull(config, "MapConfig argument cannot be null.");
        return config;
    }


    /**
     * 创建 XSS 配置
     */
    protected Map<String, Object> createMapConfig() {
        InputStream inputStream;
        try {
            inputStream = getResourceInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get resource", e);
        }
        Yaml yaml = getYaml();
        Assert.notNull(yaml, "Yaml argument cannot be null.");
        return new HumpsMap<>(yaml.load(inputStream));
    }

    protected Yaml getYaml() {
        return yaml == null ? new Yaml() : yaml;
    }

    /**
     * 获取 YAML
     *
     * @return YAML
     * @throws IOException 获取或者解析yaml文件出错时
     */
    protected InputStream getResourceInputStream() throws IOException {
        ClassPathResource resource = new ClassPathResource(classpathResource);
        return resource.getInputStream();
    }

    public void setYaml(@Nullable Yaml yaml) {
        this.yaml = yaml;
    }



}
