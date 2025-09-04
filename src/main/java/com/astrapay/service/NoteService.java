package com.astrapay.service;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;

import java.util.List;

public interface NoteService {
    NoteResponse addNote(NoteRequest request);
    List<NoteResponse> getAllNotes();
    NoteResponse updateNote(String id, NoteRequest request);
    void deleteNote(String id);
}
