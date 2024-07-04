package com.project.liar.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoreService {

    // 임시로 사용자 점수를 저장할 맵
    private Map<Long, Integer> userScores = new HashMap<>();

    // 사용자의 총 점수를 반환하는 메서드
    public int getTotalScore(long userId) {
        return userScores.getOrDefault(userId, 0);
    }

    // 사용자의 점수를 업데이트하는 메서드
    public void updateScore(long userId, int score) {
        int currentScore = userScores.getOrDefault(userId, 0);
        userScores.put(userId, currentScore + score);
    }
}
