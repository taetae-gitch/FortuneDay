package com.FortuneDay.fortune.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FortuneDay.fortune.dto.request.LoginRequest;
import com.FortuneDay.fortune.dto.request.SignUpRequest;
import com.FortuneDay.fortune.service.AuthService;
import com.FortuneDay.fortune.util.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 인증 시도
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            
            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(request.getEmail());
            
            // 쿠키 생성
            ResponseCookie cookie = ResponseCookie.from("jwt_token", token)
                .httpOnly(true)           // JavaScript에서 접근 불가 (보안)
                .secure(false)            // HTTPS에서만 전송 (개발환경에서는 false)
                .path("/")                // 모든 경로에서 접근 가능
                .maxAge(3600)             // 1시간 유효
                .sameSite("Lax")          // CSRF 방지
                .build();
            
            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("token", token);  // 응답 본문에도 포함 (선택사항)
            response.put("email", request.getEmail());
            
            return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
            
        } catch(AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인 실패");
            errorResponse.put("message", "이메일 또는 비밀번호가 올바르지 않습니다.");
            
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("테스트 엔드포인트 호출됨");
        return ResponseEntity.ok("API 서버가 정상 작동합니다!");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        System.out.println("회원가입 요청 받음: " + request.getEmail());
        System.out.println("요청 데이터: " + request.toString());
        try {
            authService.register(request);
            
            // 회원가입 성공 후 자동으로 토큰 생성 (선택사항)
            String token = jwtTokenProvider.createToken(request.getEmail());
            
            // 쿠키 생성
            ResponseCookie cookie = ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .build();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("email", request.getEmail());
            response.put("token", token);  // 자동 로그인을 위한 토큰
            
            return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
            
        } catch(IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "회원가입 실패");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
