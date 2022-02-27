package com.yongsookim.todolist.service;

import com.yongsookim.todolist.model.TodoEntity;
import com.yongsookim.todolist.persistence.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;


    public Page<TodoEntity> getTodos(String titleLike, PageRequest pageRequest) {

        if (titleLike.isEmpty()) {
            return todoRepository.findAll(pageRequest);
        }
        return todoRepository.findAllByTitleContaining(titleLike, pageRequest);
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
