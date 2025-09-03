package com.astrapay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@ApiModel(description = "Represents a note in memory")
public class Note {
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

    public Note(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
    }
}
