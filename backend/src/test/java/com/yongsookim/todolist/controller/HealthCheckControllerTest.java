package com.yongsookim.todolist.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
@RequiredArgsConstructor
class HealthCheckControllerTest {

    private final MockMvc mockMvc;
    private final HealthCheckController healthCheckController;

    @Test
    void contextLoads() {
        Assertions.assertThat(healthCheckController).isNotNull();
    }

    @Test
    void healthCheckRequest() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{\"status\":\"UP\"}"))
            .andReturn()
            .getResponse();

        System.out.println(response.getContentAsString());
    }

}