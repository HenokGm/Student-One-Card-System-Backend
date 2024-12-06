package com.socs.oneCardIDManagement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "idcards")
public class IDCard {
    @Id
    private String id; // 6-digit card ID starting with 7

    private String studentId; // Reference to the Student ID
    private String path; // Path where the ID card is stored
    private LocalDate generatedDate; // Date when the card was generated

    // Constructors, Getters, and Setters
    public IDCard(String id, String studentId, String path, LocalDate generatedDate) {
        this.id = id;
        this.studentId = studentId;
        this.path = path;
        this.generatedDate = generatedDate;
    }

    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPath() {
        return path;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }
}
