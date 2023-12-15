package com.example.NotesApp.service;

import com.example.NotesApp.model.Note;
import com.example.NotesApp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> findAuthorNameStartByCharacter(String character) {
        return noteRepository.findByAuthorStartingWith(character);
    }

    public List<Note> findByStartDatePublicationAfter(String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return noteRepository.findByDatePublicationAfter(String.valueOf(dateTime));
    }

    @Transactional
    public void create(Note note) {
        note.setDatePublication(LocalDateTime.now().toString());
        noteRepository.save(note);
    }

    @Transactional
    public void update(Note noteUpdate, Long id) {
        noteUpdate.setId(id);
        noteUpdate.setDatePublication(LocalDateTime.now().toString());
        noteRepository.save(noteUpdate);
    }

    @Transactional
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }
}
