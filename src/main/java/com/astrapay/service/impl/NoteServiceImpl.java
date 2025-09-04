package com.astrapay.service.impl;

import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.NoteResponse;
import com.astrapay.model.Note;
import com.astrapay.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final Map<String, Note> notes = new ConcurrentHashMap<>();
    @Override
    public NoteResponse addNote(NoteRequest request) {
        Note note = new Note(
                request.getTitle(),
                request.getContent()
        );
        notes.put(note.getId(), note);
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    @Override
    public List<NoteResponse> getAllNotes() {
        return notes.values().stream()
                .map(note -> new NoteResponse(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt(),
                        note.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponse updateNote(String id, NoteRequest request) {
        if (!notes.containsKey(id)) {
            throw new IllegalArgumentException("Note with id " + id + " not found");
        }

        Note note = notes.get(id);
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUpdatedAt(new Date());

        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    @Override
    public void deleteNote(String id) {
        if (!notes.containsKey(id)) throw new IllegalArgumentException("Note with id " + id + " not found");
        notes.remove(id);
    }
}
