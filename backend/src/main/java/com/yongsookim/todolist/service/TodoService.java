package com.yongsookim.todolist.service;

import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.persistence.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;



    public List<TodoEntity> getTodos() {
        return todoRepository.findAll();

    }

    public TodoEntity createNewTodo(final TodoEntity entity) {
        return todoRepository.save(entity);
    }

    public void deleteTodo(final TodoEntity entity) {

        try {
            todoRepository.delete(entity);
        } catch (Exception e) {
            log.warn("Can not delete the todo item by id:" + entity.getId(), e);
            throw new RuntimeException(e);
        }
    }


    public TodoEntity updateTodo(TodoEntity inputtedEntity) {

        try {
            TodoEntity foundTodo = todoRepository.findById(inputtedEntity.getId()).get();

            modelMapper.map(inputtedEntity, foundTodo);

            return foundTodo;

        } catch (Exception e) {
            log.error("Can not update the todo item by id:" + inputtedEntity.getId(), e);
            throw new RuntimeException(e);
        }

    }
}
