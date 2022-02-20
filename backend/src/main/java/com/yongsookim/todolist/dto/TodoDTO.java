package com.yongsookim.todolist.dto;

import com.yongsookim.todolist.model.TodoEntity;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoDTO {

    private String id;
    private String title;
    private String contents;

    public static TodoEntity convertToEntity(final TodoDTO todoDTO) {

        //TODO: 지저분하다. 개선하자.
        final UUID id = todoDTO.getId() == null ? null : UUID.fromString(todoDTO.getId());

        TodoEntity entity = TodoEntity.builder()
            .id(id)
            .title(todoDTO.getTitle())
            .contents(todoDTO.getContents())
            .build();

        return entity;
    }

}
