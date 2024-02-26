package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseForm> createUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult){
        log.info("회원 가입");

        if(bindingResult.hasErrors()){
                return handleValidationResult(bindingResult);
            }
            userService.signup(requestDto);
            return ResponseEntity.ok()
                    .body(ResponseForm.builder()
                            .httpStatus(HttpStatus.OK.value())
                            .message("회원가입 성공")
                            .build());
    }


    private ResponseEntity<ResponseForm> handleValidationResult(BindingResult bindingResult) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseForm.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST.value())
                            .message(fieldError.getDefaultMessage())
                            .build());
        }
        throw new RuntimeException("예기치 못한 오류가 발생했습니다.");
    }
}
