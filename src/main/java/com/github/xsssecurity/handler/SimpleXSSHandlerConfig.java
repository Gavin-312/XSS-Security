package com.github.xsssecurity.handler;

import com.github.xsssecurity.cleaner.XSSCleaner;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public class SimpleXSSHandlerConfig implements XSSHandlerConfig {
    private final String name;
    private final Map<String, String> initParameterMap;
    private final Enumeration<String> initParameterNames;
    private final XSSCleaner xssCleaner;

    public SimpleXSSHandlerConfig(String name, @Nullable Map<String, String> initParameterMap, XSSCleaner xssCleaner) {
        this.name = name;
        if (initParameterMap == null) {
            initParameterMap = Collections.emptyMap();
        }
        this.initParameterMap = initParameterMap;
        this.initParameterNames = Collections.enumeration(initParameterMap.keySet());
        this.xssCleaner = xssCleaner;
    }

    @Override
    public String getXSSHandlerName() {
        return name;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return initParameterNames;
    }

    @Override
    public XSSCleaner getXSSCleaner() {
        return xssCleaner;
    }
}
