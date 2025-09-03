package com.astrapay.controller;

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

import static org.mockito.ArgumentMatchers.any;
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

    private final String noteEndpoint = "/api/v1/notes";

    @Test
    void testGetAllNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(noteEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddNote() throws Exception {
        String content = "Catatan Baru";
        NoteRequest request = new NoteRequest(content);
        when(noteService.addNote(any())).thenReturn(new NoteResponse("1", content));

        mockMvc.perform(post(noteEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content));
    }

    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote("1");

        mockMvc.perform(delete(noteEndpoint + "/1"))
                .andExpect(status().isOk());
    }
}
