package com.example.NotesApp.controller;

import com.example.NotesApp.model.Note;
import com.example.NotesApp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/note")
@CrossOrigin(origins = "http://localhost:3000")
public class NotesController {
    private final NoteService service;

    @Autowired
    public NotesController(NoteService service) {
        this.service = service;
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = service.findAllNotes();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = service.findById(id);
        return note.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/{character}")
    public ResponseEntity<List<Note>> getNotesByAuthorStartsWith(@PathVariable String character) {
        List<Note> notes = service.findAuthorNameStartByCharacter(character);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/search/afterDate")
    public ResponseEntity<List<Note>> getNotesAfterDate(@RequestParam("dateTimeString") String dateTimeString) {
        List<Note> notes = service.findByStartDatePublicationAfter(dateTimeString);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createNote(@RequestBody Note note) {
        service.create(note);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNote(@RequestBody Note noteUpdate, @PathVariable Long id) {
        service.update(noteUpdate, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
