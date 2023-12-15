package com.example.NotesApp.repository;

import com.example.NotesApp.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByAuthorStartingWith(String character);

    List<Note> findByDatePublicationAfter(String dateTimeString);
}
