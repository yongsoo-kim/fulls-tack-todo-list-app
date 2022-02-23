package com.yongsookim.todolist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.persistence.TodoRepository;
import java.time.LocalDateTime;
import java.util.Optional;
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
    void create_new_todo_ok() throws Exception {

        final String testTitle = "new test title";
        final String testContents = "new test contents";

        JSONObject testRequestJson = new JSONObject();
        testRequestJson.put("title", testTitle);
        testRequestJson.put("contents", testContents);

        //TODO: "/todo"는 상수화해야할까?
        //TODO: "Location" 헤더도 상수화가능할까?
        MockHttpServletResponse response = mockMvc.perform(post("/todo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testRequestJson.toString()))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("Location", hasItems(startsWith("/todo"))))
            .andReturn()
            .getResponse();

        JSONObject responseJson = new JSONObject(response.getContentAsString());
        final String newTodoId = responseJson.getString("id");

        TodoEntity newTodoItem = todoRepository.findById(UUID.fromString(newTodoId)).get();

        assertThat(newTodoItem.getId()).isNotNull();
        assertThat(newTodoItem.getTitle()).isEqualTo(testTitle);
        assertThat(newTodoItem.isChecked()).isFalse();
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

        //TODO: 더 정교한 테스트 조건을 걸수 있을지도?(contains말고...)
        assertThat(data.length()).isGreaterThanOrEqualTo(1);
        assertTrue(data.toString().contains(testTitle));
        assertTrue(data.toString().contains(testContents));
    }


    @Test
    void update_todo_item_ok() throws Exception {
        final String testTitle = "new title";
        final String testContents = "new contents";
        final String testUserId = "testyUser";

        TodoEntity newItem = TodoEntity.builder()
            .id(UUID.randomUUID())
            .title(testTitle)
            .contents(testContents)
            .userId(testUserId)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        TodoEntity newTodo = todoRepository.save(newItem);
        UUID newTodoId = newTodo.getId();

        final boolean updateChecked = true;
        final String updateTitle = "update title";
        final String updateContents = "update contents";

        JSONObject testRequestJson = new JSONObject();
        testRequestJson.put("id", newTodoId.toString());
        testRequestJson.put("checked", updateChecked);
        testRequestJson.put("title", updateTitle);
        testRequestJson.put("contents", updateContents);

        mockMvc.perform(put("/todo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testRequestJson.toString()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        Optional<TodoEntity> todoEntityOptional = todoRepository.findById(newTodoId);

        assertThat(todoEntityOptional.isPresent()).isTrue();
        assertThat(todoEntityOptional.get().isChecked()).isEqualTo(updateChecked);
        assertThat(todoEntityOptional.get().getTitle()).isEqualTo(updateTitle);
        assertThat(todoEntityOptional.get().getContents()).isEqualTo(updateContents);
        assertThat(todoEntityOptional.get().getUpdatedDate())
            .isAfter(todoEntityOptional.get().getCreatedDate());

    }


    @Test
    void delete_todo_item_ok() throws Exception {
        final String testTitle = "delete title";
        final String testContents = "delete contents";
        final String testUserId = "testyUser";

        final TodoEntity newItem = TodoEntity.builder()
            .id(UUID.randomUUID())
            .title(testTitle)
            .contents(testContents)
            .userId(testUserId)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        final TodoEntity newEntity = todoRepository.save(newItem);
        final UUID newTodoId = newEntity.getId();

        JSONObject testRequestJson = new JSONObject();
        testRequestJson.put("id", newTodoId.toString());

        MockHttpServletResponse response = mockMvc.perform(delete("/todo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testRequestJson.toString()))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse();

        Optional<TodoEntity> foundTodo = todoRepository.findById(newTodoId);

        assertThat(foundTodo.isPresent()).isFalse();

    }
}