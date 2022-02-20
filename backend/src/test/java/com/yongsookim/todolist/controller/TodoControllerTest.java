package com.yongsookim.todolist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.persistence.TodoRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
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
    private final TodoRepository todoRepository;

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

    @Test
    void createNewTodo_ok() throws Exception {

        final String testTitle = "new test title";
        final String testContents = "new test contents";

        MockHttpServletResponse response = mockMvc.perform(post("/todo")
            .param("title", testTitle)
            .param("contents", testContents))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        JSONObject responseJson = new JSONObject(response.getContentAsString());
        final String newTodoId = responseJson.getString("id");

        TodoEntity newTodoItem = todoRepository.findById(UUID.fromString(newTodoId)).get();

        assertThat(newTodoItem.getId()).isNotNull();
        assertThat(newTodoItem.getTitle()).isEqualTo(testTitle);
        assertThat(newTodoItem.getContents()).isEqualTo(testContents);
        assertThat(newTodoItem.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(newTodoItem.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
    }


    @Test
    void get_todo_list_ok() throws Exception {

        final String testTitle = "test title for list";
        final String testContents = "test contents for list";
        final String testUserId = "testyUser";

        TodoEntity newItem = TodoEntity.builder()
            .id(UUID.randomUUID())
            .title(testTitle)
            .contents(testContents)
            .userId(testUserId)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        todoRepository.save(newItem);

        MockHttpServletResponse response = mockMvc.perform(get("/todo"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        JSONObject responseJson = new JSONObject(response.getContentAsString());

        JSONArray data = responseJson.getJSONArray("data");

        assertThat(data.length()).isGreaterThan(1);
        assertTrue(data.toString().contains(testTitle));
        assertTrue(data.toString().contains(testContents));
        assertTrue(data.toString().contains(testUserId));
    }
}