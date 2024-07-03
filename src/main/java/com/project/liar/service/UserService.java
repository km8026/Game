package com.project.liar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.liar.entity.User;
import com.project.liar.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User signupUser(String nickname, String username, String password) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("아이디가 이미 사용 중입니다.");
        }

        if (isNicknameTaken(nickname)) {
            throw new IllegalArgumentException("닉네임이 이미 사용 중입니다.");
        }

        // 비밀번호를 해싱하여 저장
        String encodedPassword = passwordEncoder.encode(password);

        // 사용자 객체 생성
        User user = new User();
        user.setNickname(nickname);
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // 데이터베이스에 저장하고 저장된 사용자 객체 반환
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("로그인 성공");
            return user; // 로그인 성공
        }
        return null; // 로그인 실패
    }
}