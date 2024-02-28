package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.LoginRequestDto;
import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyAllRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyPasswordRequestDto;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.dto.responseDto.ResponseUserList;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 기능")
    public ResponseEntity<ResponseForm> createUser(
            @Valid @RequestBody SignupRequestDto requestDto,
            BindingResult bindingResult
    ){
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

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 기능")
    public ResponseEntity<ResponseForm> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse httpServletResponse) throws Exception {
        userService.login(requestDto, httpServletResponse);

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("로그인 성공")
                        .build());
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
    @Operation(summary = "로그아웃", description = "로그아웃 기능")
    public ResponseEntity<ResponseForm> logout(
            HttpServletRequest request
    ){
        userService.logout(request);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("로그아웃 완료")
                        .build());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SELLER', 'ROLE_ADMIN')")
    @Operation(summary = "프로필 수정", description = "한줄소개, 이메일, 주소를 변경 할 수있다")
    public ResponseEntity<ResponseForm> modifyUserProfile(
            @Valid @RequestBody UserModifyAllRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ){
        log.info("회원 정보 전체 수정");

        if(bindingResult.hasErrors()){
            return handleValidationResult(bindingResult);
        }

        userService.updateAll(userDetails.getUser(), requestDto);

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("회원정보 전체 수정 성공")
                        .build());

    }

    @PatchMapping("/profile/password")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
    @Operation(summary = "비밀번호 수정", description = "비밀번호를 수정한다")
    public ResponseEntity<ResponseForm> modifyUserPassword(
            @Valid @RequestBody UserModifyPasswordRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ){
        log.info("사용자 비밀번호변경");

        if(bindingResult.hasErrors()){
            return handleValidationResult(bindingResult);
        }

        userService.updatePassword(userDetails.getUser(), requestDto);

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("비밀번호 변경 완료")
                        .build());
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "회원정보 전체 검색", description = "회원 전체 검색 - 관리자 기능")
    public ResponseEntity<ResponseForm> findAllUser() {

        log.info("회원정보 전체 검색");

        List<ResponseUserList> AllUserList = userService.findAllUser();

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .data(AllUserList)
                        .build());
    }

    @DeleteMapping("/admin/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "회원정보 삭제(비활성화)", description = "회원 삭제(비활성화) - 관리자 기능")
    public ResponseEntity<ResponseForm> deActiveUser(
            @PathVariable Long userId){

        log.info("회원정보 삭제(비활성화)");

        userService.deActiveUser(userId);

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("회원정보 삭제(비활성화) 완료")
                        .build());


    }



    private ResponseEntity<ResponseForm> handleValidationResult(
            BindingResult bindingResult
    ) {
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
