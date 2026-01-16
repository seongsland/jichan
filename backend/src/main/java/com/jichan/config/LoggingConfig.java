package com.jichan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);   // 쿼리스트링 찍기
        filter.setIncludePayload(true);       // body 찍기
        filter.setMaxPayloadLength(10000);    // body 최대 길이
        filter.setIncludeHeaders(true);       // 헤더 찍기
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        filter.setBeforeMessagePrefix(null); // Before 로그 끄기
        return filter;
    }
}