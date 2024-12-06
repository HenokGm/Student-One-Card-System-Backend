package com.socs.oneCardIDManagement.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentIDCardService {

    @Autowired
    private IDGenerationService idGenerationService;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private EmailService emailService;

    public String generateAndSendStudentIDCard(Student student) {
        try {
            // Generate the student ID
            String generatedID = idGenerationService.generateStudentID(student.getCollege(),
                    student.getEnrollmentType(), student.getEntryYear());
            student.setIdNumber(generatedID);

            // Generate the ID Card PDF
            String pdfFilePath = student.getFullName() + "_IDCard.pdf";
            pdfService.generateStudentIDCard(student);

            // Email the PDF to the student
            String email = student.getEmail(); // assuming email field exists in Student model
            emailService.sendEmailWithAttachment(email, pdfFilePath);

            return "Student ID card successfully generated and sent to: " + email;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to generate and send student ID card: " + e.getMessage();
        }
    }
}
