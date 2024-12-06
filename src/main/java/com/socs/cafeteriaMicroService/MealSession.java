package com.socs.cafeteriaMicroService;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "meal_sessions")
public class MealSession {

    @Id
    private String id; // Format: "YYYY-MM-DD_MealType"

    private String mealType; // "Breakfast", "Lunch", "Dinner"
    private String date; // e.g., "2024-10-15"
    private boolean isActive; // true if active, false if closed

    private List<String> studentIds; // List of student IDs added to the session
}
