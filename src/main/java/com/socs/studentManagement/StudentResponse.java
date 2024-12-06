package com.socs.studentManagement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentResponse {
    private Student student;
    private String photoUrl;
}
