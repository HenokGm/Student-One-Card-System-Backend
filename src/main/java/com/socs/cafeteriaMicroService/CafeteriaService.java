package com.socs.cafeteriaMicroService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeteriaService {

    private final MealSessionRepository mealSessionRepository;

    // Start a new meal session, ensuring there is no other active session
    public MealSession startMealSession(String mealType) {
        try {
            // Check if an active session already exists
            if (mealSessionRepository.existsByIsActive(true)) {
                throw new IllegalStateException("An active session already exists. Please close it first.");
            }

            // Generate session ID using current date
            String date = LocalDate.now().toString(); // Format: "YYYY-MM-DD"
            String sessionId = date + "_" + mealType;

            // Create a new meal session
            MealSession session = new MealSession();
            session.setId(sessionId);
            session.setDate(date);
            session.setMealType(mealType);
            session.setActive(true);
            session.setStudentIds(List.of());

            System.out.println("New " + mealType + " Meal session started successfully with ID: " + sessionId);
            return mealSessionRepository.save(session);
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error while starting the meal session: " + e.getMessage());
            return null;
        }
    }

    // Add a student to the active session, if not already present
    public void addStudentToActiveSession(String studentId) {
        try {
            MealSession activeSession = mealSessionRepository
                    .findByIsActive(true)
                    .orElseThrow(() -> new IllegalStateException("The Cafeteria Is Closed."));

            if (activeSession.getStudentIds().contains(studentId)) {
                throw new IllegalArgumentException(
                        "Student with ID " + studentId + " is already served with this Meal session.");
            }

            activeSession.getStudentIds().add(studentId);
            mealSessionRepository.save(activeSession);

            System.out.println("Student " + studentId + " successfully served with this Meal Session.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while serving student: " + e.getMessage());
        }
    }

    // Close the current active meal session
    public void closeActiveSession() {
        try {
            MealSession activeSession = mealSessionRepository
                    .findByIsActive(true)
                    .orElseThrow(() -> new IllegalStateException("The cafeteria is already closed."));

            activeSession.setActive(false);
            mealSessionRepository.save(activeSession);

            System.out.println("Meal session with ID: " + activeSession.getId() + " has been closed successfully.");
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("Unexpected error while closing the session: " + e.getMessage());

        }
    }
}
