package com.jichan.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
            @Override
            protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
                logger.debug(message);
            }

            @Override
            protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
                if (!request.getMethod().equals("GET")) {
                    logger.debug(message);
                }
            }
        };

        filter.setIncludeHeaders(true);
        filter.setIncludeQueryString(true);   // 쿼리스트링 찍기
        filter.setIncludePayload(true);       // body 찍기
        filter.setMaxPayloadLength(10000);    // body 최대 길이

        return filter;
    }
}