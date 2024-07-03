package com.project.liar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.liar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);  // 아이디 중복 검사 메서드
    boolean existsByNickname(String nickname);  // 닉네임 중복 검사 메서드
    public User findByUsernameAndPassword(String username, String password);
}