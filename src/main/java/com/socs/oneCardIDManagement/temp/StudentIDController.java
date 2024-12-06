package com.socs.oneCardIDManagement.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socs/student-id")
public class StudentIDController {

    @Autowired
    private StudentIDCardService studentIDCardService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateStudentIDCard(@RequestBody Student student) {
        String responseMessage = studentIDCardService.generateAndSendStudentIDCard(student);
        return ResponseEntity.ok(responseMessage);
    }
}
