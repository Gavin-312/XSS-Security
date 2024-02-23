package com.github.xsssecurity.handler;

import com.github.xsssecurity.config.FilterConfig;
import com.github.xsssecurity.config.XSSConfig;
import com.github.xsssecurity.config.XSSConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * XSS 配置处理程序映射工厂
 *
 * @author Tao.Gan
 * @date 2023/12/05
 */
public abstract class XSSConfigHandlerMappingFactory implements XSSHandlerMappingFactory {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final XSSConfigFactory xssConfigFactory;
    private final XSSHandlerMapping nullMapping = r -> null;

    protected XSSConfigHandlerMappingFactory(XSSConfigFactory xssConfigFactory) {
        this.xssConfigFactory = xssConfigFactory;
    }

    @Override
    public XSSHandlerMapping create() {
        XSSConfig xssConfig = xssConfigFactory.getXSSConfig();
        Assert.notNull(xssConfig, "XSSConfig must not be null.");

        boolean enabled = xssConfig.enabled();
        if (!enabled) {
            // 没有开启XSS防护, 不做任何操作
            if (LOG.isDebugEnabled()) {
                LOG.debug("XSS protection is not enabled.");
            }
            return nullMapping;
        }

        Collection<String> filterNames = xssConfig.getFilterNames();
        Assert.notEmpty(filterNames, "filter must not be null and must have elements.");

        List<XSSHandlerFilterConfigPair> pairs = new ArrayList<>();

        for (String filterName : filterNames) {
            FilterConfig filterConfig = xssConfig.getFilterConfig(filterName);
            Assert.notNull(filterConfig, filterName + " config must not be null.");

            XSSHandler xssHandler = createXSSHandler(filterConfig);
            Assert.notNull(xssHandler, filterName + " XSSHandler must not be null.");

            pairs.add(new XSSHandlerFilterConfigPair(xssHandler, filterConfig));
        }

        return internallyCreate(pairs);
    }

    /**
     * 创建 XSS处理程序
     *
     * @param filterConfig 过滤器配置
     * @return XSS处理程序
     */
    protected abstract XSSHandler createXSSHandler(FilterConfig filterConfig);

    /**
     * 内部创建 XSS 处理程序映射
     *
     * @return * XSS 处理程序映射
     */
    protected abstract XSSHandlerMapping internallyCreate(List<XSSHandlerFilterConfigPair> pairs);


    /**
     * XSS 处理程序和筛选器配置参数对
     *
     * @author Tao.Gan
     * @date 2023/12/05
     */
    protected static class XSSHandlerFilterConfigPair {
        private final XSSHandler xssHandler;
        private final FilterConfig filterConfig;

        private XSSHandlerFilterConfigPair(XSSHandler xssHandler, FilterConfig filterConfig) {
            this.xssHandler = xssHandler;
            this.filterConfig = filterConfig;
        }

        public XSSHandler getXssHandler() {
            return xssHandler;
        }

        public FilterConfig getFilterConfig() {
            return filterConfig;
        }
    }

}
