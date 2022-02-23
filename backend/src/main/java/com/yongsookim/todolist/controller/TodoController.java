package com.yongsookim.todolist.controller;

import com.yongsookim.todolist.dto.ResponseDTO;
import com.yongsookim.todolist.dto.TodoDTO;
import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.service.TodoService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {

    //TODO: ENUM으로 바꾸는게 좋을까?
    private static final String RESOURCE_PATH_TODO = "/todo";

    private final TodoService todoService;


    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        final String msg = "hello world";
        return ResponseEntity.ok().body(msg);
    }


    @GetMapping(RESOURCE_PATH_TODO)
    public ResponseEntity<?> retrieveTodos() {
        List<TodoEntity> todoEntities = todoService.getTodos();

        List<TodoDTO> todoDTOS = todoEntities.stream()
            .map(TodoDTO::new)
            .collect(Collectors.toList());

        ResponseDTO responseDTO = ResponseDTO.builder()
            .data(todoDTOS)
            .build();

        return ResponseEntity.ok().body(responseDTO);
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
