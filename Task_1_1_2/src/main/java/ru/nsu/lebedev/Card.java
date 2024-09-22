package ru.nsu.lebedev;

/**
 * The class responsible for one card.
 */
public class Card {
    private final String suit;
    private final String rank;
    private int value;

    /**
     * The function responsible for initializing suit, rank and value of a Card.
     */
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    /**
     * The function responsible for taking value of a Card.
     */
    public int getValue() {
        return value;
    }

    /**
     * The function responsible for changing value of an ace.
     */
    public void changeValue() {
        this.value = 1;
    }

    /**
     * The function responsible for changing sting meaning of card when we print it.
     */
    @Override
    public String toString() {
        return rank + " " + suit + " (" + value + ")";
    }
}
