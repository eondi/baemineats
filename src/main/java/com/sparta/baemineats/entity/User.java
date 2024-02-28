package com.sparta.baemineats.entity;


import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyAllRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)

    private String username;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false)

    private String description = "잘부탁드립니다.";


    @Column(nullable = false)
    private String address;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private boolean active = true;


    public User(SignupRequestDto requestDto, String encodedPassword) {
        this.username = requestDto.getUserName();
        this.password = encodedPassword;
        this.address = requestDto.getAddress();
        this.email = requestDto.getEmail();
        this.role = requestDto.getUserRoleEnum();
    }

    public User(String username, String password, String address, String email) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.email = email;
        this.role = UserRoleEnum.USER;
    }


    public void userProfileAllUpdate(UserModifyAllRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.description = requestDto.getDescription();
        this.address = requestDto.getAddress();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void deActiveUser() {
        this.active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, getUserId());
    }

}

