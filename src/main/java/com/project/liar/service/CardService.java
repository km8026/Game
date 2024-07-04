package com.project.liar.service;
import org.springframework.stereotype.Service;

import com.project.liar.entity.Card;
import com.project.liar.entity.Rank;
import com.project.liar.entity.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

@Service
public class CardService {

    private Set<Card> usedCards = new HashSet<>();
    private List<Card> deck;

    public CardService() {
        this.deck = generateDeck();
    }

    public List<Card> drawFiveCards() {
        List<Card> hand = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Card card;
            do {
                int randomIndex = random.nextInt(deck.size());
                card = deck.get(randomIndex);
            } while (usedCards.contains(card));
            hand.add(card);
            usedCards.add(card);
        }

        return hand;
    }

    public List<Card> generateDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    public Card drawUniqueCard() {
        Random random = new Random();
        Card card;
        do {
            int randomIndex = random.nextInt(deck.size());
            card = deck.get(randomIndex);
        } while (usedCards.contains(card));
        usedCards.add(card);
        return card;
    }

    public void resetUsedCards() {
        usedCards.clear();
        this.deck = generateDeck(); // 덱을 재생성합니다.
    }
}