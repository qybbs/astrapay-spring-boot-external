package com.astrapay.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Request payload to create a new note")
public class NoteRequest {
    @NotBlank(message = "Content must not be empty")
    @ApiModelProperty(
            value = "Content of the note",
            required = true,
            example = "Proyek ini bertujuan untuk mengukur kemampuan Anda..."
    )
    private String content;
}
