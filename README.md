# 介绍
XSS-Security 是一个用于防范和过滤 HTTP 请求中潜在 XSS 漏洞的安全框架。该项目旨在提供简单、可扩展的 XSS 防护机制，使开发人员能够轻松地集成并保护他们的 Web 应用程序免受 XSS 攻击。

# 功能特点
XSS 防护机制: 自动过滤和清理 HTTP 请求中的潜在 XSS 攻击载荷。

灵活的配置: 可通过配置文件定制清理策略和过滤器规则，以满足不同场景的需求。  

自定义扩展: 提供了扩展点，允许开发人员实现自定义的 XSSHandler 和 XSSHandlerMapping。  

默认拦截规则: 预置了对 header头参数、Cookie参数、表单请求和 JSON 类型请求的默认拦截规则。

##### 快速开始

配置清理策略和过滤器规则： 在项目中配置 application.yml 文件，定义清理策略和过滤器规则。

```yaml
xss:
  enabled: true
  cleaner:
    - cleaner-name: modeXSSCleaner
      property:
        mode: filter
        enableEscape: false
  filter:
    - filter-name: httpRequestMessageXSSHandler
      url-patterns: /**
      cleaner-ref: modeXSSCleaner
```

自定义扩展： 如果默认规则不满足需求，可以实现自定义的 XSSHandler 和 XSSHandlerMapping。


```java
public class CustomXSSHandler implements XSSHandler {
    // 实现自定义的 XSS 防护逻辑
}

public class CustomXSSHandlerMapping implements XSSHandlerMapping {
    // 实现自定义的 XSS 防护映射规则
}
```

# 文档
详细文档可在 [Wiki](https://www.runoob.com) 页面中找到，提供了更多配置选项、自定义扩展示例和最佳实践建议。

# 贡献
欢迎贡献代码、提交 bug 报告或提出改进建议。
