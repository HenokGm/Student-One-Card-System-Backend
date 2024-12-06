package com.socs.oneCardIDManagement;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.google.zxing.WriterException;
import com.socs.studentManagement.Student;
import com.socs.studentManagement.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/socs")
public class IDCardController {

    @Autowired
    private IDCardService idCardService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IDCardRepository idCardRepository;

    // Generate an ID card for the student by their ID
    @GetMapping(value = "/generate-id-card", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateIDCard(@RequestParam String studentId)
            throws IOException, WriterException {

        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }

        try {
            IDCard idCard = idCardService.generateAndStoreIDCard(student);
            byte[] idCardImage = Files.readAllBytes(Paths.get(idCard.getPath()));
            return ResponseEntity.ok().body(idCardImage);
        } catch (StudentIDAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    // Retrieve ID card by the card's ID
    @GetMapping("/find-card-by-id")
    public ResponseEntity<IDCard> findCardById(@RequestParam String cardId) {
        return idCardRepository.findById(cardId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));
    }

    // Retrieve ID card by student ID
    @GetMapping("/find-card-by-student-id")
    public ResponseEntity<IDCard> findCardByStudentId(@RequestParam String studentId) {
        return idCardRepository.findByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found for student"));
    }
}
