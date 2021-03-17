package com.sportzone21.server.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private Integer active;

}
