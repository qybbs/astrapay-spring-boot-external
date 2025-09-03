package com.astrapay.service;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;
import com.astrapay.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceImplTest {

    private final NoteService noteService = new NoteServiceImpl();

    @Test
    void testAddNote() {
        String content = "Aplikasi ini tidak memerlukan database; data akan disimpan secara sementara di memori aplikasi.";
        NoteRequest request = new NoteRequest(content);
        NoteResponse response = noteService.addNote(request);

        assertNotNull(response.getId());
        assertEquals(content, response.getContent());
    }

    @Test
    void testGetAllNotes() {
        noteService.addNote(new NoteRequest("Catatan 1"));
        noteService.addNote(new NoteRequest("Catatan 2"));

        List<NoteResponse> notes = noteService.getAllNotes();
        assertTrue(notes.size() >= 2);
    }

    @Test
    void testDeleteNote() {
        NoteResponse response = noteService.addNote(new NoteRequest("Catatan yang akan terhapus"));
        noteService.deleteNote(response.getId());

        List<NoteResponse> notes = noteService.getAllNotes();
        assertTrue(notes.stream().noneMatch(
                n -> n.getId().equals(response.getId())
        ));
    }

    @Test
    void testDeleteNonExistingNoteThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> noteService.deleteNote("invalid-id"));
    }
}
