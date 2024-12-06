package com.socs.cafeteriaMicroService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/socs/cafeteria")
@RequiredArgsConstructor
public class CafeteriaController {

    private final CafeteriaService cafeteriaService;

    // Start a new meal session
    @PostMapping("/start")
    public ResponseEntity<MealSession> startSession(@RequestParam String mealType) {
        MealSession session = cafeteriaService.startMealSession(mealType);
        return ResponseEntity.ok(session);
    }

    // Add a student to the active session
    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestParam String studentId) {
        cafeteriaService.addStudentToActiveSession(studentId);
        return ResponseEntity.ok("Student added successfully.");
    }

    // Close the current active meal session
    @PostMapping("/close")
    public ResponseEntity<String> closeSession() {
        cafeteriaService.closeActiveSession();
        return ResponseEntity.ok("Session closed successfully.");
    }
}
