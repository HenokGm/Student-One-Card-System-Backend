package com.socs.studentManagement;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvValidationException;

import io.jsonwebtoken.io.IOException;

import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/socs/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;
    private static final String UPLOAD_DIR = "C:/uploads/pictures/";

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    // an api to register a new student

    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("departement") String departement,
            @RequestParam("gender") String gender,
            @RequestParam("college") String college,
            @RequestParam("programmeType") String programmeType,
            @RequestParam("enrollmentType") String enrollmentType,
            @RequestParam("entry_year") String entryYear,
            @RequestParam("picture") MultipartFile picture) {

        try {
            // Save picture or perform other logic
            String picturePath = "C:/uploads/pictures/" + picture.getOriginalFilename();
            picture.transferTo(new java.io.File(picturePath));

            RegisterRequest registerRequest = new RegisterRequest(
                    null, // Assuming studentId will be generated
                    firstname,
                    lastname,
                    email,
                    password,
                    departement,
                    gender,
                    college,
                    programmeType,
                    enrollmentType,
                    entryYear,
                    picturePath // Save the picture path in the student record
            );

            // Call the service to register the student
            service.register(registerRequest);

            return ResponseEntity.ok("Student registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to register student.");
        }
    }

    @GetMapping("get-student-info")
    public ResponseEntity<?> getStudentInfo(@RequestParam String studentId) {
        Student student = service.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    // an api to find all students

    @GetMapping("/get-all-students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    // an api to import csv file for students
    @PostMapping("/import")
    public ResponseEntity<?> importStudents(@RequestParam("file") MultipartFile file)
            throws IOException, CsvValidationException, java.io.IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid CSV file.");
        }

        try {
            service.importStudentsFromCsv(file);
            return ResponseEntity.ok("Students imported successfully.");
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the CSV file.");
        }
    }
    // an api to count number of students in database

    @GetMapping("/count")
    public ResponseEntity<?> countStudents() {
        return ResponseEntity.ok(service.countStudents());
    }

    @PostMapping("/upload-picture")
    public ResponseEntity<String> uploadPicture(@RequestParam("picture") MultipartFile picture)
            throws IllegalStateException, java.io.IOException {
        try {
            java.io.File directory = new java.io.File(UPLOAD_DIR);
            if (!directory.exists() && !directory.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not create upload directory");
            }

            // Ensure consistent use of paths
            String fileName = picture.getOriginalFilename();
            java.io.File destinationFile = new java.io.File(directory, fileName);
            picture.transferTo(destinationFile);

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

}