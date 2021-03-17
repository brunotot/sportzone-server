package com.sportzone21.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String objectName;
    private String fieldName;
    private Object rejectedValue;
    private String messageCode;
    private String message;

}
