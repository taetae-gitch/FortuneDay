package com.FortuneDay.fortune.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    // JWT 인증이 필요 없는 경로들
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
        "/api/login", 
        "/api/signUp"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        System.out.println("JWT 필터 동작: " + method + " " + path);

        // JWT 인증이 필요 없는 경로는 필터 우회
        if (EXCLUDE_URLS.contains(path)) {
            System.out.println("JWT 필터 우회됨: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        // API 경로가 아니면 필터 우회 (웹 페이지 등)
        if (!path.startsWith("/api")) {
            System.out.println("API 경로가 아님, 필터 우회: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("JWT 토큰 발견: " + token.substring(0, Math.min(20, token.length())) + "...");

            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("JWT 인증 성공: " + email);
            } else {
                System.out.println("JWT 토큰 검증 실패");
            }
        } else {
            System.out.println("JWT 토큰 없음");
        }
        
        filterChain.doFilter(request, response);
    }
}
