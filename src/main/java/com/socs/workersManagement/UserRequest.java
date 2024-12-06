package com.socs.workersManagement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String picture;
    private Role role;

}
