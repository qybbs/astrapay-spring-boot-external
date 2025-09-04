package com.astrapay.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@ApiModel(description = "Response payload for note data")
public class NoteResponse {
    @ApiModelProperty(
            value = "Unique identifier of the note",
            example = "b91b6a3e-****-982d015fa321"
    )
    private String id;

    @ApiModelProperty(
            value = "Title of the note",
            example = "Hiring Project Challenge: Product Developer"
    )
    private String title;

    @ApiModelProperty(
            value = "Content of the note",
            example = "Simple Notes Application"
    )
    private String content;

    @ApiModelProperty(
            value = "Timestamp of note creation",
            example = "04-09-2025"
    )
    private Date createdAt;

    @ApiModelProperty(
            value = "Timestamp of note updation",
            example = "04-09-2025"
    )
    private Date updatedAt;
}
