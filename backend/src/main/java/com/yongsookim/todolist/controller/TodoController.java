package com.yongsookim.todolist.controller;

import com.yongsookim.todolist.config.PaginationProperties;
import com.yongsookim.todolist.dto.ResponseDTO;
import com.yongsookim.todolist.dto.TodoDTO;
import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.service.TodoService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {

    //TODO: ENUM으로 바꾸는게 좋을까?
    private static final String RESOURCE_PATH_TODO = "/todo";

    private final TodoService todoService;

    private final PaginationProperties paginationProperties;


    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        final String msg = "hello world";
        return ResponseEntity.ok().body(msg);
    }


    @GetMapping(RESOURCE_PATH_TODO)
    public ResponseEntity<?> retrieveTodos(
        @RequestParam("title_like") Optional<String> titleLike,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size) {

        //TODO: 객체화 시키는것도 괜찮을듯?
        PageRequest pageRequest = PageRequest.of(
            page.orElse(paginationProperties.getDefaultPage()),
            size.orElse(paginationProperties.getDefaultSize()),
            Direction.DESC,
            "updatedDate"
        );

        Page<TodoEntity> todoPage = todoService.getTodos(titleLike.orElse(""), pageRequest);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(todoPage.getTotalElements()));

        List<TodoDTO> todoDTOS = todoPage.getContent().stream()
            .map(TodoDTO::new)
            .collect(Collectors.toList());

        ResponseDTO responseDTO = ResponseDTO.builder()
            .data(todoDTOS)
            .build();

        return ResponseEntity.ok().headers(responseHeaders).body(responseDTO);
    }


    @PostMapping(RESOURCE_PATH_TODO)
    public ResponseEntity<?> createNewTodo(@RequestBody TodoDTO todoDTO) {
        todoDTO.setId(null);

        // TODO: 불변객체에 관해서 정리할것.
        TodoEntity entity = TodoDTO.convertToEntity(todoDTO);

        entity.setUserId("testyUser");

        TodoEntity createdTodo = todoService.createNewTodo(entity);

        return ResponseEntity
            .created(URI.create(RESOURCE_PATH_TODO + "/" + createdTodo.getId()))
            .body(new TodoDTO(createdTodo));
    }


    @PutMapping(RESOURCE_PATH_TODO)
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todoDTO) {

        TodoEntity inputEntity = TodoDTO.convertToEntity(todoDTO);
        inputEntity.setUserId("testUser");

        TodoEntity updatedEntity = todoService.updateTodo(inputEntity);

        return ResponseEntity.ok().body(new TodoDTO(updatedEntity));
    }


    @DeleteMapping(RESOURCE_PATH_TODO)
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO todoDTO) {
        TodoEntity entity = TodoDTO.convertToEntity(todoDTO);

        todoService.deleteTodo(entity);

        return ResponseEntity.noContent().build();
    }

}
