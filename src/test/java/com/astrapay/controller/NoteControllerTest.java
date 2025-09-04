package com.astrapay.controller;

import com.astrapay.constant.ConstantVariable;
import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;
import com.astrapay.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ConstantVariable.NOTE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Notes retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testAddNote() throws Exception {
        String title = "Catatan Baru";
        String content = "Ini adalah catatan baru";
        Date now = new Date();
        NoteRequest request = new NoteRequest(title, content);
        when(noteService.addNote(any())).thenReturn(new NoteResponse("1", title, content, now, now));

        mockMvc.perform(post(ConstantVariable.NOTE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Note created successfully"))
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.content").value(content));
    }

    @Test
    void testUpdateNote() throws Exception {
        String noteId = "1";
        String updatedTitle = "Catatan Diperbarui";
        String updatedContent = "Ini adalah catatan yang telah diperbarui";
        Date createdAt = new Date();
        Date updatedAt = new Date();

        NoteRequest updateRequest = new NoteRequest(updatedTitle, updatedContent);
        NoteResponse updateResponse = new NoteResponse(noteId, updatedTitle, updatedContent, createdAt, updatedAt);

        when(noteService.updateNote(eq(noteId), any())).thenReturn(updateResponse);

        mockMvc.perform(put(ConstantVariable.NOTE_ENDPOINT + "/" + noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Note updated successfully"))
                .andExpect(jsonPath("$.data.id").value(noteId))
                .andExpect(jsonPath("$.data.title").value(updatedTitle))
                .andExpect(jsonPath("$.data.content").value(updatedContent));
    }

    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote("1");

        mockMvc.perform(delete(ConstantVariable.NOTE_ENDPOINT + "/1"))
                .andExpect(status().isOk());
    }
}
