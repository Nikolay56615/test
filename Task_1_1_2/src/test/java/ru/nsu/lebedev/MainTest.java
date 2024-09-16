package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MainTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testCardValues() {
        Card twoOfHearts = new Card("Черви", "Двойка", 2);
        Card aceOfSpades = new Card("Пики", "Туз", 11);
        Card kingOfClubs = new Card("Трефы", "Король", 10);
        assertEquals(2, twoOfHearts.getValue());
        assertEquals(11, aceOfSpades.getValue());
        assertEquals(10, kingOfClubs.getValue());
    }

    @Test
    void testHandValueWithAce() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card("Черви", "Туз", 11));
        hand.add(new Card("Бубны", "Шестерка", 6));
        int handValue = Main.calculateHandValue(hand);
        assertEquals(17, handValue);
        hand.add(new Card("Трефы", "Девятка", 9));
        handValue = Main.calculateHandValue(hand);
        assertEquals(16, handValue);
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
    void testDealerHitsBelow17() {
        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Черви", "Четверка", 4));
        dealerHand.add(new Card("Бубны", "Шестерка", 6));
        while (Main.calculateHandValue(dealerHand) < 17) {
            dealerHand.add(deck.drawCard());
        }
        assertTrue(Main.calculateHandValue(dealerHand) >= 17);
    }

    @Test
    void testPlayerBust() {
        List<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Десятка", 10));
        playerHand.add(new Card("Бубны", "Восьмерка", 8));
        playerHand.add(new Card("Трефы", "Пятерка", 5));
        assertTrue(Main.calculateHandValue(playerHand) > 21);
    }

    @Test
    void testPlayerWinsWithBlackjack() {
        List<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Туз", 11));
        playerHand.add(new Card("Пики", "Король", 10));
        assertEquals(21, Main.calculateHandValue(playerHand));
        assertTrue(Main.calculateHandValue(playerHand) == 21);
    }

    @Test
    void testDeckShuffle() {
        deck.shuffle();
        assertNotNull(deck.drawCard());
        assertEquals(51, deck.getRemainingCardsCount());
    }

/*
    @Test
    void testMain() throws IOException {
        String simulatedInput = "0\n";
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.main();
        String output = outputStream.toString();
        String expectedOutput = "Добро пожаловать в Блэкджек!\r\n"
                +
                "Выберите, сколько раундов игры вы хотите?\r\n"
                +
                "Игра закончилась вничью! Счёт 0:0.\r\n"
                +
                "Игра окончена. Спасибо за участие!\r\n";
        assertEquals(expectedOutput, output);
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testRoundOutcomePlayerWins() {
        String simulatedInput = "нет\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        int result = Main.playRound(1);
        assertTrue(result == 1, "Ожидался выигрыш игрока.");
        System.setIn(originalIn);
    }

    @Test
    void testRoundOutcomeDealerWins() {
        String simulatedInput = "нет\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        int result = Main.playRound(1);
        assertTrue(result == -1, "Ожидался проигрыш игрока.");
        System.setIn(originalIn);
    }
*/
}