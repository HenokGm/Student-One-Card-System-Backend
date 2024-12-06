package com.socs.studentManagement;

import lombok.RequiredArgsConstructor;

import org.apache.el.stream.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.jsonwebtoken.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository repository;
    private String GeneratedStudentId;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (Student) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    // Function to generate student ID
    private String generateStudentId(String college, String programType, String enrollmentType, String year) {
        // Get the first letter of the program type (e.g., 'U' for 'Undergraduate')
        String programCode = programType.substring(0, 1).toLowerCase();

        // Generate the prefix: e.g., 'ur' for 'U' + 'Regular'
        String prefix = programCode + enrollmentType.toLowerCase().charAt(0);

        // Count students already registered in this college and year
        // long existingCount = StreamSupport.stream(repository.findAll().spliterator(),
        // false)
        // .filter(student -> student.getDepartement().equalsIgnoreCase(college) &&
        // student.getEntry_year().endsWith(year))
        // .count();

        // Generate the 6-digit number (incrementing)
        long nextNumber = 190001 + repository.findAll().size();

        // Format the final student ID: e.g., 'eitm/ur190001/24'
        return String.format("%s/%s%06d/%s",
                college.toLowerCase(), prefix, nextNumber, year.substring(2));
    }

    // Register a new student with a generated ID
    public void register(RegisterRequest request) {
        // Generate the student ID based on provided details
        java.util.Optional<Student> existingStudent = repository.findByEmail(request.getEmail());
        if (existingStudent.isPresent()) {
            throw new DataIntegrityViolationException(
                    "A student with this email already exists: " + request.getEmail());
        }
        if (request.getStudentId() == null) {

            GeneratedStudentId = generateStudentId(
                    request.getCollege(),
                    request.getProgrammeType(),
                    request.getEnrollmentType(),
                    request.getEntry_year());
        } else {
            GeneratedStudentId = request.getStudentId();
        }
        // Create and save the student entity
        var student = Student.builder()
                .studentId(GeneratedStudentId)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .departement(request.getDepartement())
                .gender(request.getGender())
                .college(request.getCollege())
                .programmeType(request.getProgrammeType())
                .enrollmentType(request.getEnrollmentType())
                .entry_year(request.getEntry_year())
                .picture(request.getPicture())
                .build();

        repository.save(student);
    }

    // function to search for a student by email

    public Student getStudentByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }
    // function to get all students

    public Iterable<Student> getAllStudents() {
        return repository.findAll();
    }

    @SuppressWarnings("unchecked")
    public Student getStudentById(String studentId) {
        return (Student) repository.findById(studentId).orElse(null);
    }

    // importing csv file of student list
    public void importStudentsFromCsv(MultipartFile file)
            throws IOException, CsvValidationException, java.io.IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); // Skip header row (if present)

            while ((line = csvReader.readNext()) != null) {
                Student student = Student.builder()
                        .studentId(line[0])
                        .firstname(line[1])
                        .lastname(line[2])
                        .email(line[3])
                        .password(passwordEncoder.encode(line[4])) // Encrypt password
                        .departement(line[5])
                        .entry_year(line[6])
                        .build();
                students.add(student);
            }
        }

        repository.saveAll(students); // Save all students in batch
    }

    // count number of students
    public long countStudents() {
        return repository.count();
    }

}