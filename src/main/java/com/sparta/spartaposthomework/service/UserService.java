package com.sparta.spartaposthomework.service;

import com.sparta.spartaposthomework.dto.LoginRequestDto;
import com.sparta.spartaposthomework.dto.SignupRequestDto;
import com.sparta.spartaposthomework.entity.User;
import com.sparta.spartaposthomework.jwt.JwtUtil;
import com.sparta.spartaposthomework.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw  new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 생성 후 DB에 저장
        User user = new User(signupRequestDto);
        userRepository.save(user);

        return "회원가입 성공";
    }

    // 로그인
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 유저가 없습니다.")
        );

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getUserRoleEnum()));

        return "로그인 완료";

    }
}