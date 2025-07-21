package com.FortuneDay.fortune.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FortuneDay.fortune.dto.request.SignUpRequest;
import com.FortuneDay.fortune.entity.User;
import com.FortuneDay.fortune.entity.UserStatus;
import com.FortuneDay.fortune.entity.Role;
import com.FortuneDay.fortune.entity.Provider;
import com.FortuneDay.fortune.entity.UserGrade;
import com.FortuneDay.fortune.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(SignUpRequest request) {
        //이메일 중복 체크
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        //비밀번호 암호화
        String encoderPassword = passwordEncoder.encode(request.getPassword());

        //생년월일 변환
        LocalDateTime birthDate = null;
        if(request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
            try {
                // ISO 형식 (예: "1990-01-01T00:00:00") 또는 날짜만 (예: "1990-01-01")
                if(request.getBirthDate().contains("T")) {
                    birthDate = LocalDateTime.parse(request.getBirthDate());
                } else {
                    // 날짜만 있는 경우 시간을 00:00:00으로 설정
                    birthDate = LocalDateTime.parse(request.getBirthDate() + "T00:00:00");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다. (예: 1990-01-01)");
            }
        }

        //User Entity 생성 및 저장
        User user = User.builder()
            .email(request.getEmail())
            .password(encoderPassword)
            .name(request.getName())
            .nickname(request.getNickname())
            .gender(request.getGender())
            .birthDate(birthDate)
            .location(request.getLocation())
            .status(UserStatus.ACTIVE)
            .role(Role.USER)
            .provider(Provider.LOCAL)
            .grade(UserGrade.NORMAL)
            .providerId("local_" + request.getEmail()) // 로컬 회원가입용 고유 ID
            .build();

        userRepository.save(user);
    }
}
