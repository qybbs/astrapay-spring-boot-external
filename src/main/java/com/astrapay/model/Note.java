package com.astrapay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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

    public Note(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
