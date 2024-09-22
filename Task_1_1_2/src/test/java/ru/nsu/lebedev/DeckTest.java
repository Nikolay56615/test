package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testWhenDeckIsEnd() {
        for (int i = 0; i < 53; i++) {
            if (i == 52) {
                assertNull(deck.drawCard());
                break;
            }
            deck.drawCard();
        }
    }

    @Test
    void testDeckShuffle() {
        deck.shuffle();
        assertNotNull(deck.drawCard());
        assertEquals(51, deck.getRemainingCardsCount());
    }
}