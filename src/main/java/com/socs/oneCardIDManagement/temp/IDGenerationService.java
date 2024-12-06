package com.socs.oneCardIDManagement.temp;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class IDGenerationService {

    public String generateStudentID(String college, String enrollmentType, int entryYear) {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generate a 6-digit random number

        // Extract the last two digits of the academic year
        String yearShort = String.valueOf(entryYear).substring(2);

        // Format the student ID as requested: eitm/ur123456/24
        return String.format("%s/%s%d/%s",
                college.toLowerCase(),
                enrollmentType.toLowerCase().substring(0, 2),
                randomNumber,
                yearShort);
    }
}
