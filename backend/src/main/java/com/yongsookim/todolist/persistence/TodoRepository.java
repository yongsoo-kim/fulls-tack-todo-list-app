package com.yongsookim.todolist.persistence;

import com.yongsookim.todolist.model.TodoEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
    List<TodoEntity> findAllByUserId(String userId);
}
