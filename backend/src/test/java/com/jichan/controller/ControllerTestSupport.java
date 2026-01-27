package com.jichan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jichan.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public abstract class ControllerTestSupport {
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // 공통 인증 객체 설정
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("1", null,
                Collections.emptyList());

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/").principal(auth)
                        .accept(MediaType.APPLICATION_JSON))
                .build();
    }
}
