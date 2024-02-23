package com.github.xsssecurity;

import com.github.xsssecurity.cleaner.ModeXSSCleaner;
import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.config.XSSConfigFactory;
import com.github.xsssecurity.handler.SpringXSSHandlerRequestPathMappingFactoryBean;
import com.github.xsssecurity.handler.XSSHandler;
import com.github.xsssecurity.config.map.YamlXSSConfigFactory;
import com.github.xsssecurity.handler.XSSHandlerMapping;
import com.github.xsssecurity.handler.support.HttpRequestMessageXSSHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(prefix = "xss", name = "enabled", havingValue = "true")
public class XSSFilterConfigurer {

    @Bean
    public FactoryBean<XSSHandlerMapping> xssHandlerMappingFactory() {
        XSSConfigFactory configFactory = new YamlXSSConfigFactory("application.yml");
        return new SpringXSSHandlerRequestPathMappingFactoryBean(configFactory);
    }

    @Bean("modeXSSCleaner")
    public XSSCleaner xssCleaner() {
        return new ModeXSSCleaner();
    }

    @Bean("httpRequestMessageXSSHandler")
    public XSSHandler xssHandler() {
        return new HttpRequestMessageXSSHandler();
    }

    @Bean
    public FilterRegistrationBean<XSSServletFilter> xssServletFilter(XSSHandlerMapping mapping) {
        FilterRegistrationBean<XSSServletFilter> filter = new FilterRegistrationBean<>();
        filter.setOrder(Integer.MIN_VALUE);
        filter.addUrlPatterns("/*");
        filter.setName("xssServletFilter");
        filter.setFilter(new XSSServletFilter(mapping));
        return filter;
    }

}
