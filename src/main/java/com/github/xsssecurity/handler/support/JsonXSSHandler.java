package com.github.xsssecurity.handler.support;

import com.github.xsssecurity.cleaner.XSSCleaner;
import com.github.xsssecurity.handler.XSSHandlerConfig;
import com.github.xsssecurity.handler.XSSMatcherHandler;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JSON XSS 处理程序
 *
 * @author Tao.Gan
 * @date 2023/11/30
 */
public class JsonXSSHandler implements XSSMatcherHandler {

    private ObjectMapper cleanerMapper;

    @Override
    public void init(XSSHandlerConfig config) {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(String.class, new StringXssDeserializer(config.getXSSCleaner()));
        mapper.registerModule(simpleModule);
        this.cleanerMapper = mapper;
    }

    @Override
    public HttpServletRequest handle(HttpServletRequest request) {
        return new JSONXSSHttpServletRequestWrapper(request, cleanerMapper);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String contentType = request.getContentType();
        return StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
    }

    static class StringXssDeserializer extends JsonDeserializer<String> {

        private final XSSCleaner cleaner;

        public StringXssDeserializer(XSSCleaner cleaner) {
            this.cleaner = cleaner;
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String source = p.getText().trim();
            return cleaner.clean(source);
        }
    }

    /**
     * JSON XSSHTTP servlet 请求包装器
     *
     * @author Tao.Gan
     * @date 2023/11/30
     */
    static class JSONXSSHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final ObjectMapper cleanerMapper;

        public JSONXSSHttpServletRequestWrapper(HttpServletRequest request, ObjectMapper cleanerMapper) {
            super(request);
            this.cleanerMapper = cleanerMapper;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            ServletInputStream stream = super.getInputStream();
            if (stream.isFinished()) {
                return stream;
            }
            Object object;
            try {
                object = cleanerMapper.readValue(stream, Object.class);
            } catch (InvalidDefinitionException ex) {
                throw new HttpMessageReadException("Type definition error: " + ex.getType(), ex);
            } catch (JsonProcessingException ex) {
                throw new HttpMessageReadException("JSON parse error: " + ex.getOriginalMessage(), ex);
            }
            String cleaned = cleanerMapper.writeValueAsString(object);
            byte[] body;
            if (StringUtils.hasText(cleaned)) {
                body = cleaned.getBytes(StandardCharsets.UTF_8);
            } else {
                body = new byte[0];
            }
            return new BodyInputStream(body);
        }
    }
}
