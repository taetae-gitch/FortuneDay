package com.FortuneDay.fortune.dto.request;

import com.FortuneDay.fortune.entity.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private Gender gender;
    private String birthDate;
    private String location;
}
