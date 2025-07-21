package com.FortuneDay.fortune.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.FortuneDay.fortune.entity.User;
import com.FortuneDay.fortune.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String nickname = null;

        if ("kakao".equalsIgnoreCase(registrationId)) {
            System.out.println("카카오 attributes: " + attributes);

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                if (profile != null) {
                    nickname = (String) profile.get("nickname");
                }
            }
        }

        if (email == null || nickname == null) {
            throw new OAuth2AuthenticationException("카카오 로그인 정보가 부족합니다. (email 또는 nickname 누락)");
        }

        final String finalEmail = email;
        final String finalNickname = nickname;

        userRepository.findByEmail(email)
            .orElseGet(() -> registerNewUser(finalEmail, finalNickname));

        return oAuth2User;
    }

    private User registerNewUser(String email, String nickname) {
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password("소셜로그인") // 소셜로그인은 비밀번호를 사용하지 않음
                .build();   

        return userRepository.save(user);
    }
}
