package com.socs.oneCardIDManagement.temp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "oneCardID")
public class Student {
    @Id
    private String id;
    private String fullName;
    private String idNumber; // Generated ID
    private String gender;
    private String email;
    private String department;
    private int entryYear;
    private String enrollmentType; // e.g., Undergraduate Regular
    private String college; // e.g., EITM
    private String studentPhoto; // URL or Base64 encoded image
}
