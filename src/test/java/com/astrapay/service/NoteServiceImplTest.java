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
        String title = "Ketentuan";
        String content = "Aplikasi ini tidak memerlukan database; data akan disimpan secara sementara di memori aplikasi.";
        NoteRequest request = new NoteRequest(title, content);
        NoteResponse response = noteService.addNote(request);

        assertNotNull(response.getId());
        assertEquals(title, response.getTitle());
        assertEquals(content, response.getContent());
    }

    @Test
    void testGetAllNotes() {
        noteService.addNote(new NoteRequest("Catatan 1", "Ini adalah catatan 1"));
        noteService.addNote(new NoteRequest("Catatan 2", "Ini adalah catatan 2"));

        List<NoteResponse> notes = noteService.getAllNotes();
        assertTrue(notes.size() >= 2);
    }

    @Test
    void testDeleteNote() {
        NoteResponse response = noteService.addNote(new NoteRequest("Catatan terhapus","Ini adalah catatan yang akan terhapus"));
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
