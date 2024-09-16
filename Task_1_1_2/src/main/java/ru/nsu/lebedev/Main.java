package ru.nsu.lebedev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Class with realisation of game process.
 */
public class Main {
    /**
     * A function in which interim results and final results of the game are announced.
     */
    public static void main() throws IOException {
        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Выберите, сколько раундов игры вы хотите?");
        Scanner scanner = new Scanner(System.in);
        int totalRounds = scanner.nextInt();
        scanner.nextLine();
        int playerWins = 0;
        int dealerWins = 0;
        for (int round = 1; round <= totalRounds; round++) {
            int result = playRound(round);
            if (result == 1) {
                playerWins++;
                System.out.println("Вы выиграли раунд! Счёт: "
                    +
                    playerWins
                    +
                    ":"
                    +
                    dealerWins
                    +
                    " в вашу пользу.");
            } else if (result == -1) {
                dealerWins++;
                System.out.println("Дилер выиграл раунд! Счёт: "
                    +
                    playerWins
                    +
                    ":"
                    +
                    dealerWins
                    +
                    " в пользу дилера.");
            } else {
                System.out.println("Раунд закончился вничью! Счёт: "
                    +
                    playerWins
                    +
                    ":"
                    +
                    dealerWins
                    +
                    ".");
            }
            System.out.println("\n--------------------------\n");
        }
        if (playerWins > dealerWins) {
            System.out.println("Поздравляем! Вы выиграли игру со счётом "
                +
                playerWins
                +
                ":"
                +
                dealerWins
                +
                "!");
        } else if (dealerWins > playerWins) {
            System.out.println("Увы, дилер выиграл игру со счётом "
                +
                dealerWins
                +
                ":"
                +
                playerWins
                +
                ".");
        } else {
            System.out.println("Игра закончилась вничью! Счёт "
                +
                playerWins
                +
                ":"
                +
                dealerWins
                +
                ".");
        }
        System.out.println("Игра окончена. Спасибо за участие!");
    }

    /**
     * Еhe function in which the main functionality of the game is implemented.
     * The player's and dealer's moves, determining the victory in the rendezvous.
     */
    public static int playRound(int round) {
        Deck deck = new Deck();
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        playerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        System.out.println("\nРаунд " + round);
        System.out.println("Дилер раздал карты");
        System.out.println("Ваши карты: " + playerHand + " > " + calculateHandValue(playerHand));
        System.out.println("Карты дилера: [" + dealerHand.get(0) + ", <закрытая карта>]");
        if (calculateHandValue(playerHand) == 21) {
            System.out.println("Блэкджек!");
            return 1;
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nХотите взять ещё карту? (да/нет): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("да")) {
                playerHand.add(deck.drawCard());
                System.out.println("Ваши карты: "
                    +
                    playerHand
                    +
                    " > "
                    +
                    calculateHandValue(playerHand));
                if (calculateHandValue(playerHand) > 21) {
                    System.out.println("Вы проиграли раунд! Перебор.");
                    return -1;
                }
            } else {
                break;
            }
        }
        System.out.println("Карты дилера: " + dealerHand + " > " + calculateHandValue(dealerHand));
        if (calculateHandValue(dealerHand) == 21) {
            System.out.println("Блэкджек!");
            return -1;
        }
        while (calculateHandValue(dealerHand) < 17) {
            dealerHand.add(deck.drawCard());
            System.out.println("Дилер берёт карту...");
            System.out.println("Карты дилера: " + dealerHand
                +
                " > "
                +
                calculateHandValue(dealerHand));
        }
        int playerTotal = calculateHandValue(playerHand);
        int dealerTotal = calculateHandValue(dealerHand);
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("Вы выиграли раунд!");
            return 1;
        } else if (playerTotal == dealerTotal) {
            System.out.println("Ничья в раунде!");
            return 0;
        } else {
            System.out.println("Дилер выиграл раунд.");
            return -1;
        }
    }

    /**
     * Scoring points in a hand, considering aces.
     */
    public static int calculateHandValue(List<Card> hand) {
        int totalValue = 0;
        int aceCount = 0;
        for (Card card : hand) {
            totalValue += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        while (totalValue > 21 && aceCount > 0) {
            for (Card card : hand) {
                if (card.getValue() == 11) {
                    card.changeValue();
                    break;
                }
            }
            totalValue -= 10;
            aceCount--;
        }
        return totalValue;
    }
}
