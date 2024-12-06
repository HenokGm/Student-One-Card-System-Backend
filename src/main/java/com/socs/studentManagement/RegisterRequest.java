package com.socs.studentManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String studentId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String departement;
    private String gender;
    private String college;
    private String programmeType;
    private String enrollmentType;
    private String entry_year;
    private String picture;
}
