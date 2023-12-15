package com.example.NotesApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author")
    private String author;

    @Column(name = "content")
    private String content;

    @Column(name = "date_publication")
    private String datePublication;

    @Column(name = "reminder_date")
    private String reminderDate;

}
