package com.github.xsssecurity.handler;

import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.config.CleanerConfig;
import com.github.xsssecurity.config.FilterConfig;
import com.github.xsssecurity.config.XSSConfigFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * Spring XSS 处理程序请求路径映射工厂Bean
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public class SpringXSSHandlerRequestPathMappingFactoryBean
        extends XSSConfigHandlerRequestPathMappingFactory
        implements ApplicationContextAware, FactoryBean<XSSHandlerMapping> {

    private ApplicationContext applicationContext;

    public SpringXSSHandlerRequestPathMappingFactoryBean(XSSConfigFactory xssConfigFactory) {
        super(xssConfigFactory);
    }

    @Override
    public XSSHandlerMapping getObject() throws Exception {
        return super.create();
    }

    @Override
    public Class<?> getObjectType() {
        return XSSHandlerMapping.class;
    }

    @Override
    protected XSSHandler getXSSHandler(FilterConfig filterConfig) {
        String filterName = filterConfig.getFilterName();
        if (!applicationContext.containsBean(filterName)) {
            return super.getXSSHandler(filterConfig);
        }
        return applicationContext.getBean(filterName, XSSHandler.class);
    }

    @Nullable
    @Override
    protected XSSCleaner createXSSCleaner(@Nullable CleanerConfig cleanerConfig) {
        if (cleanerConfig == null) {
            return null;
        }
        String cleanerName = cleanerConfig.getCleanerName();
        if (!applicationContext.containsBean(cleanerName)) {
            return super.createXSSCleaner(cleanerConfig);
        }
        XSSCleaner xssCleaner = applicationContext.getBean(cleanerName, XSSCleaner.class);
        xssCleaner.setProperties(cleanerConfig.getProperty());
        return xssCleaner;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
