package com.yongsookim.todolist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class TodoControllerTest {

    private final MockMvc mockMvc;
    private final TodoController todoController;

    @Test
    void contextLoads() {
        assertThat(todoController).isNotNull();
    }

    @Test
    void hello() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
            .andReturn()
            .getResponse();
    }
}