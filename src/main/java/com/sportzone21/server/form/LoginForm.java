package com.sportzone21.server.form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotBlank(message = "Jebem ti bgoa")
    @Size(min = 5, max = 256, message = "[this.is.message]")
    private String username;
    @NotBlank
    @Size(min = 5, max = 256)
    private String password;

}
