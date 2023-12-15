package com.example.NotesApp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoadTest {


    private final MockMvc mockMvc;

    private static final int NUMBER_OF_REQUESTS = 10;

    @Autowired
    public LoadTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetAllNotes() throws Exception {
        runLoadTest(() -> {
            try {
                mockMvc.perform(get("/note"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testGetNoteById() throws Exception {
        runLoadTest(() -> {
            try {
                mockMvc.perform(get("/note/1"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testGetNotesByAuthorStartsWith() throws Exception {
        runLoadTest(() -> {
            try {
                mockMvc.perform(get("/note/search/John"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testGetNotesAfterDate() throws Exception {
        runLoadTest(() -> {
            try {
                mockMvc.perform(get("/note/search/afterDate").param("dateTimeString", "2023-01-01T12:00:00"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testCreateNote() throws Exception {
        String jsonPayload = "{\"title\":\"Test Note\",\"content\":\"Test Content\",\"author\":\"John Doe\"}";
        runLoadTest(() -> {
            try {
                mockMvc.perform(post("/note").content(jsonPayload).contentType("application/json"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testUpdateNote() throws Exception {
        String jsonPayload = "{\"title\":\"Updated Note\",\"content\":\"Updated Content\",\"author\":\"Jane Doe\"}";
        runLoadTest(() -> {
            try {
                mockMvc.perform(put("/note/1").content(jsonPayload).contentType("application/json"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testDeleteNote() throws Exception {
        runLoadTest(() -> {
            try {
                mockMvc.perform(delete("/note/1"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void runLoadTest(Runnable action) throws Exception {
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            action.run();
        }
    }
}
