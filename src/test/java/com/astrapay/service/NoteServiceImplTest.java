package com.astrapay.service;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;
import com.astrapay.exception.NoteNotFoundException;
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
    void testUpdateNote() {
        // Add a note first
        String originalTitle = "Judul Asli";
        String originalContent = "Konten asli";
        NoteRequest addRequest = new NoteRequest(originalTitle, originalContent);
        NoteResponse addedNote = noteService.addNote(addRequest);

        // Update the note
        String updatedTitle = "Judul Diperbarui";
        String updatedContent = "Konten telah diperbarui";
        NoteRequest updateRequest = new NoteRequest(updatedTitle, updatedContent);
        NoteResponse updatedNote = noteService.updateNote(addedNote.getId(), updateRequest);

        // Verify the update
        assertNotNull(updatedNote);
        assertEquals(addedNote.getId(), updatedNote.getId());
        assertEquals(updatedTitle, updatedNote.getTitle());
        assertEquals(updatedContent, updatedNote.getContent());
        assertEquals(addedNote.getCreatedAt(), updatedNote.getCreatedAt()); // CreatedAt should remain the same
        assertTrue(updatedNote.getUpdatedAt().after(addedNote.getCreatedAt()) ||
                updatedNote.getUpdatedAt().equals(addedNote.getCreatedAt())); // UpdatedAt should be newer or equal
    }

    @Test
    void testUpdateNonExistingNoteThrowsException() {
        NoteRequest updateRequest = new NoteRequest("Judul", "Konten");
        assertThrows(NoteNotFoundException.class, () ->
                noteService.updateNote("non-existing-id", updateRequest));
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
        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNote("invalid-id"));
    }
}
