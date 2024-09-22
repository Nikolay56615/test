package ru.nsu.lebedev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class responsible for deck.
 */
public class Deck {
    private List<Card> cards;

    /**
     * The function responsible for initializing of a deck.
     */
    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Черви", "Бубны", "Трефы", "Пики"};
        String[] ranks = {"Двойка", "Тройка", "Четверка", "Пятерка", "Шестерка",
            "Семерка", "Восьмерка", "Девятка", "Десятка", "Валет", "Дама", "Король", "Туз"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                cards.add(new Card(suit, ranks[i], values[i]));
            }
        }
        shuffle();
    }

    /**
     * The function responsible for shuffling of a deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * The function responsible for taking one card of a deck.
     */
    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }

    /**
     * The function responsible for adding a card to the top of the deck.
     */
    public void addCardToTop(Card card) {
        if (card != null) {
            cards.add(0, card);
        }
    }

    /**
     * The function responsible for returning size of deck.
     */
    public int getRemainingCardsCount() {
        return cards.size();
    }
}
