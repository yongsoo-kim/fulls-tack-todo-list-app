package com.yongsookim.todolist.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yongsookim.todolist.model.TodoEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TodoDTOTest {

    @Test
    void createNew() {
        TodoDTO todoDTO = TodoDTO.builder().build();
        assertThat(todoDTO).isNotNull();
    }


    @Test
    void convertDTOtoEntity_id_null() {
        TodoDTO todoDTO = TodoDTO.builder()
            .id(null)
            .title("test title")
            .contents("test contents")
            .build();

        TodoEntity todoEntity = TodoDTO.convertToEntity(todoDTO);

        assertThat(todoEntity.getId()).isNull();
        assertThat(todoEntity.getTitle()).isEqualTo("test title");
        assertThat(todoEntity.getContents()).isEqualTo("test contents");
        assertThat(todoEntity.getCreatedDate()).isNull();
        assertThat(todoEntity.getUpdatedDate()).isNull();
    }

    @Test
    void convertDTOtoEntity_id_not_null() {
        final String testUUID = "d35412ef-bfc4-4dc0-bbab-64af8bff498c";

        TodoDTO todoDTO = TodoDTO.builder()
            .id(testUUID)
            .build();

        TodoEntity todoEntity = TodoDTO.convertToEntity(todoDTO);

        assertThat(todoEntity.getId()).isEqualTo(
            UUID.fromString(testUUID)
        );
    }

}