package com.astrapay.controller;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.BaseResponse;
import com.astrapay.dto.response.NoteResponse;
import com.astrapay.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequestMapping("/api/v1/notes")
@Api(tags = "Notes API")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @ApiOperation(
            value = "Get all notes",
            notes = "Retrieve a list of all available notes"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved notes")
    })
    public ResponseEntity<BaseResponse<List<NoteResponse>>> getAllNotes() {
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Notes retrieved successfully",
                noteService.getAllNotes()
        ));
    }

    @PostMapping
    @ApiOperation(
            value = "Add a new note",
            notes = "Create a new note with provided content"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Note created successfully"),
            @ApiResponse(code = 400, message = "Invalid input, content must not be empty")
    })
    public ResponseEntity<BaseResponse<NoteResponse>> addNote(@Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(
                true,
                "Note created successfully",
                noteService.addNote(request)
        ));
    }

    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update a note",
            notes = "Update a note with provided content"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Note updated successfully"),
            @ApiResponse(code = 400, message = "Invalid input, content must not be empty")
    })
    public ResponseEntity<BaseResponse<NoteResponse>> updateNote(
            @PathVariable String id,
            @Valid @RequestBody NoteRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(
                true,
                "Note updated successfully",
                noteService.updateNote(id, request)
        ));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Delete a note",
            notes = "Delete an existing note by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Note deleted successfully"),
            @ApiResponse(code = 404, message = "Note not found with provided ID")
    })
    public ResponseEntity<BaseResponse<Void>> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().body(new BaseResponse<>(
                true,
                "Note deleted successfully"
        ));
    }
}
