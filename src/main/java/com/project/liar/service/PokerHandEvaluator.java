package com.project.liar.service;


import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.liar.entity.Card;
import com.project.liar.entity.Rank;
import com.project.liar.entity.Suit;
@Service
public class PokerHandEvaluator {

    // 패의 종류를 나타내는 열거형
    public enum HandType {
        ROYAL_FLUSH,
        STRAIGHT_FLUSH,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        FLUSH,
        STRAIGHT,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    public static HandType evaluateHand(List<Card> hand) {
        // 정렬된 카드의 랭크 리스트
        List<Rank> ranks = hand.stream().map(Card::getRank).sorted().collect(Collectors.toList());

        // 카드의 랭크와 슈트를 각각 맵으로 저장
        Map<Rank, Long> rankCounts = hand.stream().collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));
        Map<Suit, Long> suitCounts = hand.stream().collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        boolean isFlush = suitCounts.size() == 1;
        boolean isStraight = isStraight(ranks);
        boolean isRoyal = isRoyal(ranks);

        if (isFlush && isRoyal) {
            return HandType.ROYAL_FLUSH;
        } else if (isFlush && isStraight) {
            return HandType.STRAIGHT_FLUSH;
        } else if (rankCounts.containsValue(4L)) {
            return HandType.FOUR_OF_A_KIND;
        } else if (rankCounts.containsValue(3L) && rankCounts.containsValue(2L)) {
            return HandType.FULL_HOUSE;
        } else if (isFlush) {
            return HandType.FLUSH;
        } else if (isStraight) {
            return HandType.STRAIGHT;
        } else if (rankCounts.containsValue(3L)) {
            return HandType.THREE_OF_A_KIND;
        } else if (rankCounts.size() == 3) {
            return HandType.TWO_PAIR;
        } else if (rankCounts.containsValue(2L)) {
            return HandType.ONE_PAIR;
        } else {
            return HandType.HIGH_CARD;
        }
    }

    private static boolean isStraight(List<Rank> ranks) {
        int[] ordinalValues = ranks.stream().mapToInt(Rank::ordinal).toArray();
        for (int i = 1; i < ordinalValues.length; i++) {
            if (ordinalValues[i] != ordinalValues[i - 1] + 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRoyal(List<Rank> ranks) {
        return ranks.containsAll(Arrays.asList(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE));
    }
}
