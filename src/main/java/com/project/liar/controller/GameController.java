package com.project.liar.controller; 
import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.liar.entity.Card;
import com.project.liar.service.CardService;
import com.project.liar.service.PokerHandEvaluator;
import com.project.liar.service.PokerHandEvaluator.HandType;
import com.project.liar.service.ScoreService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ScoreService scoreService;

    private static final String PLAYER_HAND_SESSION_ATTR = "playerHand";
    private static final String HAND_TYPE_SESSION_ATTR = "handType";
    private static final String TOTAL_SCORE_SESSION_ATTR = "totalScore";
    private static final String RESLECT_COUNT_SESSION_ATTR = "reselectCount";

    @GetMapping("/game")
    public String showGamePage(Model model, HttpSession session) {
        List<Card> playerHand = (List<Card>) session.getAttribute(PLAYER_HAND_SESSION_ATTR);
        if (playerHand == null || playerHand.isEmpty()) {
            cardService.resetUsedCards();
            playerHand = cardService.drawFiveCards();
            session.setAttribute(PLAYER_HAND_SESSION_ATTR, playerHand);
        }
        HandType handType = PokerHandEvaluator.evaluateHand(playerHand);
        model.addAttribute("handType", handType);
        model.addAttribute("handScore", calculateScore(handType));

        long userId = 1L; // 예시로 사용자 ID를 설정

        // 세션에서 총 점수를 가져와 모델에 추가
        int totalScore = scoreService.getTotalScore(userId);
        session.setAttribute(TOTAL_SCORE_SESSION_ATTR, totalScore); // 세션에 점수 저장
        model.addAttribute("totalScore", totalScore);

        // 교체 버튼 클릭 횟수를 세션에서 가져오거나 초기화
        Integer reselectCount = (Integer) session.getAttribute(RESLECT_COUNT_SESSION_ATTR);
        if (reselectCount == null) {
            reselectCount = 0;
        }
        model.addAttribute("reselectCount", reselectCount);

        return "game";
    }

    @PostMapping("/game/reselect-cards")
    public String reselectCards(
            @RequestParam(value = "selectedIndexes", required = false) List<Integer> selectedIndexes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (selectedIndexes == null) {
            selectedIndexes = new ArrayList<>();
        }

        List<Card> playerHand = (List<Card>) session.getAttribute(PLAYER_HAND_SESSION_ATTR);
        List<Card> newHand = new ArrayList<>(playerHand);

        for (Integer index : selectedIndexes) {
            Card newCard = cardService.drawUniqueCard();
            newHand.set(index, newCard);
        }

        session.setAttribute(PLAYER_HAND_SESSION_ATTR, newHand); // 업데이트된 카드 정보 세션에 저장

        // 교체 버튼 클릭 횟수 증가
        Integer reselectCount = (Integer) session.getAttribute(RESLECT_COUNT_SESSION_ATTR);
        if (reselectCount == null) {
            reselectCount = 0;
        }
        reselectCount++;
        session.setAttribute(RESLECT_COUNT_SESSION_ATTR, reselectCount);

        // 점수 차감
        long userId = 1L; // 예시로 사용자 ID를 설정
        int penalty = calculatePenalty(reselectCount);
        scoreService.updateScore(userId, -penalty);

        redirectAttributes.addFlashAttribute("playerCards", newHand);
        return "redirect:/game";
    }

    @PostMapping("/game/confirm-cards")
    public String confirmCards(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Card> playerHand = (List<Card>) session.getAttribute(PLAYER_HAND_SESSION_ATTR);
        HandType handType = PokerHandEvaluator.evaluateHand(playerHand);
        session.setAttribute(HAND_TYPE_SESSION_ATTR, handType); // 패의 종류 세션에 저장
        long userId = 1L; // 예시로 사용자 ID를 설정

        // 점수 계산
        int score = calculateScore(handType);
        // 점수 업데이트
        scoreService.updateScore(userId, score);

        // 새로운 카드 뽑기
        cardService.resetUsedCards();
        List<Card> newHand = cardService.drawFiveCards();
        session.setAttribute(PLAYER_HAND_SESSION_ATTR, newHand); // 새로운 카드 정보 세션에 저장

        // 교체 버튼 클릭 횟수 초기화
        session.removeAttribute(RESLECT_COUNT_SESSION_ATTR);

        return "redirect:/game";
    }

    private int calculateScore(HandType handType) {
        switch (handType) {
            case ROYAL_FLUSH:
                return 500000;
            case STRAIGHT_FLUSH:
                return 50000;
            case FOUR_OF_A_KIND:
                return 10000;
            case FULL_HOUSE:
                return 5000;
            case FLUSH:
                return 3000;
            case STRAIGHT:
                return 2000;
            case THREE_OF_A_KIND:
                return 1500;
            case TWO_PAIR:
                return 600;
            case ONE_PAIR:
                return 300;
            case HIGH_CARD:
                return 100;
            default:
                return 0;
        }
    }

    private int calculatePenalty(int reselectCount) {
        switch (reselectCount) {
            case 1:
                return 0;
            case 2:
                return 500;
            case 3:
                return 1000;
            case 4:
                return 2000;
            case 5:
                return 5000;
            default:
                return 0;
        }
    }
    
    @GetMapping("/poker")
    public String startPage() {
        return "GameStart";
    }
}
