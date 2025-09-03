package com.astrapay.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

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
            value = "Content of the note",
            example = "Hiring Project Challenge: Product Developer"
    )
    private String content;
}
