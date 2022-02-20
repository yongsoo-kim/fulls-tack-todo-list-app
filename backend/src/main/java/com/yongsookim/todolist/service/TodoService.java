package com.yongsookim.todolist.service;

import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.persistence.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;


    public List<TodoEntity> getTodos(final String userId) {
        return todoRepository.findAllByUserId(userId);
    }

    public TodoEntity createNewTodo(final TodoEntity entity) {
        return todoRepository.save(entity);
    }


}
