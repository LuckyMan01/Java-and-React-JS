package com.example.NotesApp;

import com.example.NotesApp.controller.NotesController;
import com.example.NotesApp.model.Note;
import com.example.NotesApp.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotesControllerTest {
    @Mock
    private NoteService noteService;

    @InjectMocks
    private NotesController notesController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notesController).build();
    }

    @Test
    public void testGetAllNotes() throws Exception {
        List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteService.findAllNotes()).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/note"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(notes.size()));

        verify(noteService, times(1)).findAllNotes();
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testGetNoteById() throws Exception {
        Long noteId = 1L;
        Note note = new Note();
        note.setId(noteId);
        when(noteService.findById(noteId)).thenReturn(Optional.of(note));

        mockMvc.perform(MockMvcRequestBuilders.get("/note/{id}", noteId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(noteId));

        verify(noteService, times(1)).findById(noteId);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testGetNotesByAuthorStartsWith() throws Exception {
        String authorPrefix = "John";
        List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteService.findAuthorNameStartByCharacter(authorPrefix)).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/note/search/{character}", authorPrefix))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(notes.size()));

        verify(noteService, times(1)).findAuthorNameStartByCharacter(authorPrefix);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testGetNotesAfterDate() throws Exception {
        String dateTimeString = "2023-01-01T12:00:00";  // Assuming ISO date-time format
        List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteService.findByStartDatePublicationAfter(dateTimeString)).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/note/search/afterDate")
                        .param("dateTimeString", dateTimeString))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(notes.size()));

        verify(noteService, times(1)).findByStartDatePublicationAfter(dateTimeString);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testCreateNote() throws Exception {
        Note note = new Note();
        mockMvc.perform(MockMvcRequestBuilders.post("/note")
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(noteService, times(1)).create(note);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testUpdateNote() throws Exception {
        Long noteId = 1L;
        Note updatedNote = new Note();
        mockMvc.perform(MockMvcRequestBuilders.put("/note/{id}", noteId)
                        .content(asJsonString(updatedNote))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(noteService, times(1)).update(updatedNote, noteId);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void testDeleteNote() throws Exception {
        Long noteId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/note/{id}", noteId))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).delete(noteId);
        verifyNoMoreInteractions(noteService);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
