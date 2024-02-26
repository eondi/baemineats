package com.sparta.baemineats.entity;


import com.sparta.baemineats.dto.requestDto.UserModifyAllRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

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

    public User(String username, String password, String address, String email,UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.email = email;
        this.role = role;
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

}
