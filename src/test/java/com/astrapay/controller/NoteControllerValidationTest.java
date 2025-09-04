package com.astrapay.controller;

import com.astrapay.constant.ConstantVariable;
import com.astrapay.dto.request.NoteRequest;
import com.astrapay.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddNoteValidationError() throws Exception {
        NoteRequest invalidRequest = new NoteRequest("", "");

        mockMvc.perform(post(ConstantVariable.NOTE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNoteValidationError() throws Exception {
        String noteId = "1";
        NoteRequest invalidRequest = new NoteRequest("", "");

        mockMvc.perform(put(ConstantVariable.NOTE_ENDPOINT + "/" + noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNoteNotFound() throws Exception {
        String nonExistentId = "999";
        NoteRequest validRequest = new NoteRequest("Valid Title", "Valid Content");

        doThrow(new IllegalArgumentException("Note with id " + nonExistentId + " not found"))
                .when(noteService).updateNote(eq(nonExistentId), any());

        mockMvc.perform(put(ConstantVariable.NOTE_ENDPOINT + "/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNoteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Note with id 999 not found"))
                .when(noteService).deleteNote("999");

        mockMvc.perform(delete(ConstantVariable.NOTE_ENDPOINT + "/999"))
                .andExpect(status().isNotFound());
    }
}
