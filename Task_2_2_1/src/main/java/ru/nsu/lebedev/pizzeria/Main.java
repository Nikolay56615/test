package ru.nsu.lebedev.pizzeria;

import java.io.IOException;

/**
 * Main class.
 */
public class Main {

    /**
     * Main method.
     */
    public static void main(String[] args) {
        try {
            new Thread(
                    new Pizzeria(
                            50, "noBakers.json",
                            "Nno"
                    )
            ).start();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}