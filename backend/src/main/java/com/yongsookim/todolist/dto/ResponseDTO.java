package com.yongsookim.todolist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    @JsonInclude(Include.NON_NULL)
    private List<?> error;

    private List<?> data;

}
