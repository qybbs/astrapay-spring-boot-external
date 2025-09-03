package com.astrapay.service;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;

import java.util.List;

public interface NoteService {
    NoteResponse addNote(NoteRequest request);
    List<NoteResponse> getAllNotes();
    void deleteNote(String id);
}
