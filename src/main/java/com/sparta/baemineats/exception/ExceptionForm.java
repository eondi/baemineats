package com.sparta.baemineats.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ExceptionForm {
    private int statusCode;
    private HttpStatus state;
    private String message;
}
