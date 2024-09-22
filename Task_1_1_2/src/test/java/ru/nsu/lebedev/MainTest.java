package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    }

    @Test
    void testDealerWins() {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Десятка", 10));
        playerHand.add(new Card("Бубны", "Семерка", 7));
        dealerHand.add(new Card("Пики", "Десятка", 10));
        dealerHand.add(new Card("Трефы", "Валет", 10));
        Main.GameResult result = Main.winChecking(playerHand, dealerHand);
        assertEquals(Main.GameResult.DEALER_WINS, result);
    }

    @Test
    void testDraw() {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Десятка", 10));
        playerHand.add(new Card("Бубны", "Валет", 10));
        dealerHand.add(new Card("Пики", "Десятка", 10));
        dealerHand.add(new Card("Трефы", "Валет", 10));
        Main.GameResult result = Main.winChecking(playerHand, dealerHand);
        assertEquals(Main.GameResult.DRAW, result);
    }

    @Test
    void testPlayerWinsAfterDealerBust() {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Десятка", 10));
        playerHand.add(new Card("Бубны", "Семерка", 7));
        dealerHand.add(new Card("Пики", "Десятка", 10));
        dealerHand.add(new Card("Трефы", "Девятка", 9));
        dealerHand.add(new Card("Бубны", "Тройка", 3));
        Main.GameResult result = Main.winChecking(playerHand, dealerHand);
        assertEquals(Main.GameResult.PLAYER_WINS, result);
    }

    @Test
    void testPlayerTurnNoBust() {
        String simulatedInput = "нет\n";
        final InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        List<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Десятка", 10));
        playerHand.add(new Card("Бубны", "Шестерка", 6));
        Main.GameResult result = Main.playersTurn(deck, playerHand);
        assertEquals(Main.GameResult.NO_ONE_WIN_YET, result);
        System.setIn(originalIn);
    }

    @Test
    void testMainOutput() {
        String simulatedInput = "0\n";
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.main(new String[]{});
        String output = outputStream.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Выберите, сколько раундов игры вы хотите?"));
        assertTrue(output.contains("Игра окончена. Спасибо за участие!"));
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testPlayerBlackjackWinsDealerRegular() {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        playerHand.add(new Card("Черви", "Туз", 11));
        playerHand.add(new Card("Бубны", "Король", 10));
        dealerHand.add(new Card("Пики", "Десятка", 10));
        dealerHand.add(new Card("Трефы", "Семерка", 7));
        Main.GameResult result = Main.winChecking(playerHand, dealerHand);
        assertEquals(Main.GameResult.PLAYER_WINS, result);
    }

    @Test
    void testPlayRoundDealerWins() {
        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Трефы", "Туз", 11));  // Дилеру
        dealerHand.add(new Card("Пики", "Король", 10));   // Дилеру
        Main.GameResult result = Main.dealersTurn(deck, dealerHand);
        assertEquals(Main.GameResult.DEALER_WINS, result);
    }

    @Test
    void testPlayRoundDraw() {
        String simulatedInput = "нет\n";
        final InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        deck.drawCard();
        deck.drawCard();
        deck.drawCard();
        deck.drawCard();
        deck.addCardToTop(new Card("Черви", "Девятка", 9));
        deck.addCardToTop(new Card("Бубны", "Девятка", 9));
        deck.addCardToTop(new Card("Трефы", "Девятка", 9));
        deck.addCardToTop(new Card("Пики", "Девятка", 9));
        Main.GameResult result = Main.playRound(1, deck);
        assertEquals(Main.GameResult.DRAW, result);
        System.setIn(originalIn);
    }

    @Test
    void testDealersTurn() {
        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Черви", "Девятка", 9));
        dealerHand.add(new Card("Бубны", "Девятка", 9));
        Main.GameResult result = Main.dealersTurn(deck, dealerHand);
        assertEquals(Main.GameResult.NO_ONE_WIN_YET, result);
        dealerHand.add(new Card("Трефы", "Тройка", 3)); // Дилер получает 21
        result = Main.dealersTurn(deck, dealerHand);
        assertEquals(Main.GameResult.DEALER_WINS, result);
    }
}