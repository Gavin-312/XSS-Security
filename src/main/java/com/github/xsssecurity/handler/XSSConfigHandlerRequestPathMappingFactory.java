package com.github.xsssecurity.handler;

import com.github.xsssecurity.cleaner.ModeXSSCleaner;
import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.config.CleanerConfig;
import com.github.xsssecurity.config.FilterConfig;
import com.github.xsssecurity.config.XSSConfigFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * XSS 配置处理程序请求路径映射工厂
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class XSSConfigHandlerRequestPathMappingFactory extends XSSConfigHandlerMappingFactory {

    public XSSConfigHandlerRequestPathMappingFactory(XSSConfigFactory xssConfigFactory) {
        super(xssConfigFactory);
    }

    @Override
    protected XSSHandlerMapping internallyCreate(List<XSSHandlerFilterConfigPair> pairs) {
        XSSHandlerRequestPathMapping mapping = new XSSHandlerRequestPathMapping();
        for (XSSHandlerFilterConfigPair pair : pairs) {
            FilterConfig filterConfig = pair.getFilterConfig();
            XSSHandler xssHandler = pair.getXssHandler();
            mapping.addPathMapping(filterConfig.getUrlPatterns(), xssHandler);
        }
        return mapping;
    }

    @Override
    protected XSSHandler createXSSHandler(FilterConfig filterConfig) {

        XSSHandler xssHandler = getXSSHandler(filterConfig);

        XSSCleaner xssCleaner = createXSSCleaner(filterConfig.getCleanerConfig());
        if (xssCleaner == null) {
            xssCleaner = new ModeXSSCleaner();
        }

        xssHandler.init(new SimpleXSSHandlerConfig(filterConfig.getFilterName(),
                filterConfig.getInitParameters(), xssCleaner));

        return xssHandler;
    }


    /**
     * 创建 XSS清理策略
     *
     * @param cleanerConfig XSS清理策略配置
     * @return XSS清理策略
     */
    @Nullable
    protected XSSCleaner createXSSCleaner(@Nullable CleanerConfig cleanerConfig) {
        if (cleanerConfig == null) {
            return null;
        }
        String cleanerClass = cleanerConfig.getCleanerClass();
        Assert.notNull(cleanerClass, "cleaner [" + cleanerConfig.getCleanerName() + "] class cannot be null.");

        XSSCleaner xssCleaner = newInstance(cleanerClass);
        xssCleaner.setProperties(cleanerConfig.getProperty());
        return xssCleaner;
    }

    /**
     * 获取 XSS 处理程序
     *
     * @param filterConfig 过滤器配置
     * @return XSS 处理程序
     */
    protected XSSHandler getXSSHandler(FilterConfig filterConfig) {
        String filterClass = filterConfig.getFilterClass();
        Assert.notNull(filterClass, "filter [" + filterConfig.getFilterName() + "] class cannot be null.");
        return newInstance(filterClass);
    }

    private <T> T newInstance(String className) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(className);
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException
                 | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
