package com.astrapay.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Request payload to create a new note")
public class NoteRequest {

    @NotBlank(message = "Title must not be empty")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @ApiModelProperty(
            value = "Title of the note",
            required = true,
            example = "Proyek Catatan Simpel"
    )
    private String title;

    @NotBlank(message = "Content must not be empty")
    @Size(max = 5000, message = "Content must not exceed 5000 characters")
    @ApiModelProperty(
            value = "Content of the note",
            required = true,
            example = "Proyek ini bertujuan untuk mengukur kemampuan Anda..."
    )
    private String content;
}


