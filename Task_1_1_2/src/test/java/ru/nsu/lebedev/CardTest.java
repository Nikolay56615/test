package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CardTest {
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
    void testToString() {
        // Проверка работы метода toString для карты
        Card card = new Card("Черви", "Туз", 11);
        String expected = "Туз Черви (11)";
        assertEquals(expected, card.toString());
    }
}