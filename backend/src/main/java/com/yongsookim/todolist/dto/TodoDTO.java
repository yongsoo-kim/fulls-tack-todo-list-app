package com.yongsookim.todolist.dto;

import com.yongsookim.todolist.model.TodoEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TodoDTO {

    private String id;
    private String title;
    private boolean checked;
    private String contents;

    public TodoDTO(TodoEntity entity) {
        this.id = entity.getId().toString();
        this.checked = entity.isChecked();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
    }

    public static TodoEntity convertToEntity(final TodoDTO todoDTO) {

        //TODO: 지저분하다. 개선하자.
        final UUID id = todoDTO.getId() == null ? null : UUID.fromString(todoDTO.getId());

        TodoEntity entity = TodoEntity.builder()
            .id(id)
            .title(todoDTO.getTitle())
            .checked(todoDTO.isChecked())
            .contents(todoDTO.getContents())
            .build();

        return entity;
    }

}
