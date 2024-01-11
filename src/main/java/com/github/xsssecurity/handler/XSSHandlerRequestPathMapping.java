package com.github.xsssecurity.handler;

import com.github.xsssecurity.handler.support.XSSHandlerConvertMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * XSS 处理程序请求映射
 *
 * @author Tao.Gan
 * @date 2023/12/04
 */
public class XSSHandlerRequestPathMapping implements XSSHandlerMapping {
    private final PathMatcher pathMatcher;

    private final Map<String, XSSHandler> handlerMap = new HashMap<>();

    public XSSHandlerRequestPathMapping() {
        this(new AntPathMatcher());
    }

    public XSSHandlerRequestPathMapping(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher must not be null.");
        this.pathMatcher = pathMatcher;
    }

    @Override
    public XSSHandler getXSSHandler(HttpServletRequest request) {
        String path = request.getRequestURI();
        List<String> matchingPatterns = new ArrayList<>();
        for (String pattern : handlerMap.keySet()) {
            // 判断该url是否与目标匹配，匹配的话加入到list
            if (pathMatcher.match(pattern, path)) {
                matchingPatterns.add(pattern);
            }
        }
        // 从匹配的url中选取一个最合适的
        String bestMatch = null;
        if (!matchingPatterns.isEmpty()) {
            if (matchingPatterns.size() != 1) {
                Comparator<String> comparator = pathMatcher.getPatternComparator(path);
                // 进行排序
                matchingPatterns.sort(comparator);
            }
            // 拿到第一个最匹配的
            bestMatch = matchingPatterns.get(0);
        }
        if (bestMatch == null) {
            return null;
        }
        return handlerMap.get(bestMatch);
    }

    public void addPathMapping(String urlPattern, XSSHandler xssHandler) {
        this.addPathMapping(Collections.singletonList(urlPattern), xssHandler);
    }

    public void addPathMapping(String urlPattern, XSSMatcherHandler matcherHandler) {
        this.addPathMapping(Collections.singletonList(urlPattern), matcherHandler);
    }


    public void addPathMapping(Collection<String> urlPatterns, XSSHandler xssHandler) {
        this.addPathMapping(urlPatterns, XSSHandlerConvertMatcher.convert(xssHandler), xssHandler);
    }

    public void addPathMapping(Collection<String> urlPatterns, XSSMatcher xssMatcher, XSSHandler xssHandler) {
        Assert.notEmpty(urlPatterns, "URL patterns must not be empty.");
        Assert.notNull(xssMatcher, "XSSMatcher must not be null.");
        Assert.notNull(xssHandler, "XSSHandler must not be null.");
        for (String urlPattern : urlPatterns) {
            this.handlerMap.computeIfAbsent(urlPattern,
                    k -> new InternallyXSSMatcherHandler(xssMatcher, xssHandler));
        }
    }

    public void addPathMapping(Collection<String> urlPatterns, XSSMatcherHandler matcherHandler) {
        Assert.notEmpty(urlPatterns, "URL patterns must not be empty.");
        Assert.notNull(matcherHandler, "XSSMatcherHandler must not be null.");
        for (String urlPattern : urlPatterns) {
            this.handlerMap.computeIfAbsent(urlPattern,
                    k -> new InternallyXSSMatcherHandler(matcherHandler, matcherHandler));
        }
    }

    /**
     * 内部 XSS 路径匹配器处理程序
     *
     * @author Tao.Gan
     * @date 2023/12/05
     */
    private static class InternallyXSSMatcherHandler implements XSSHandler {
        private final XSSMatcher xssMatcher;
        private final XSSHandler xssHandler;

        private InternallyXSSMatcherHandler(XSSMatcher xssMatcher, XSSHandler xssHandler) {
            this.xssMatcher = xssMatcher;
            this.xssHandler = xssHandler;
        }

        @Override
        public HttpServletRequest handle(HttpServletRequest request) {
            return xssMatcher.matches(request) ? xssHandler.handle(request) : request;
        }
    }

}
