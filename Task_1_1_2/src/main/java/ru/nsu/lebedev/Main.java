package ru.nsu.lebedev;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class with realisation of game process.
 */
public class Main {
    public static int winCount = 21;
    public static int maximumDealerClearance = 17;

    public enum GameResult {
        PLAYER_WINS,
        DEALER_WINS,
        DRAW,
        NO_ONE_WIN_YET
    }

    /**
     * A function in which interim results and final results of the game are announced.
     */
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Выберите, сколько раундов игры вы хотите?");
        Scanner scanner = new Scanner(System.in);
        int totalRounds = scanner.nextInt();
        scanner.nextLine();
        int playerWins = 0;
        int dealerWins = 0;
        for (int round = 1; round <= totalRounds; round++) {
            Deck deck = new Deck();
            GameResult result = playRound(round, deck);  // Изменено на GameResult
            switch (result) {
                case PLAYER_WINS:
                    playerWins++;
                    System.out.println("Вы выиграли раунд! Счёт: "
                            + playerWins + ":" + dealerWins + " в вашу пользу.");
                    break;
                case DEALER_WINS:
                    dealerWins++;
                    System.out.println("Дилер выиграл раунд! Счёт: "
                            + playerWins + ":" + dealerWins + " в пользу дилера.");
                    break;
                case DRAW:
                    System.out.println("Раунд закончился вничью! Счёт: "
                            + playerWins + ":" + dealerWins + ".");
                    break;
            }
            System.out.println("\n--------------------------\n");
        }
        if (playerWins > dealerWins) {
            System.out.println("Поздравляем! Вы выиграли игру со счётом "
                    + playerWins + ":" + dealerWins + "!");
        } else if (dealerWins > playerWins) {
            System.out.println("Увы, дилер выиграл игру со счётом "
                    + dealerWins + ":" + playerWins + ".");
        } else {
            System.out.println("Игра закончилась вничью! Счёт "
                    + playerWins + ":" + dealerWins + ".");
        }
        System.out.println("Игра окончена. Спасибо за участие!");
    }

    /**
     * A function where cards are dealt and the game begins.
     */
    public static GameResult playRound(int round, Deck deck) {
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
        GameResult firstTrigger = playersTurn(deck, playerHand);
        if (firstTrigger != GameResult.NO_ONE_WIN_YET) {
            return firstTrigger;
        }
        GameResult secondTrigger = dealersTurn(deck, dealerHand);
        if (secondTrigger != GameResult.NO_ONE_WIN_YET) {
            return secondTrigger;
        }
        return winChecking(playerHand, dealerHand);
    }

    /**
     * The function at which the player's move occurs.
     */
    public static GameResult playersTurn(Deck deck, List<Card> playerHand) {
        if (calculateHandValue(playerHand) == winCount) {
            System.out.println("Блэкджек!");
            return GameResult.DEALER_WINS;
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nХотите взять ещё карту? (да/нет): ");
            String input = scanner.nextLine().toLowerCase();
            switch (input) {
                case "да":
                    playerHand.add(deck.drawCard());
                    System.out.println("Ваши карты: "
                            + playerHand + " > " + calculateHandValue(playerHand));
                    if (calculateHandValue(playerHand) > winCount) {
                        System.out.println("Вы проиграли раунд! Перебор.");
                        return GameResult.DEALER_WINS;
                    }
                    break;
                case "нет":
                    return GameResult.NO_ONE_WIN_YET;
                default:
                    System.out.println("Неверная команда, попробуйте ещё раз!");
            }
        }
    }

    /**
     * The function at which the player's move occurs.
     */
    public static GameResult dealersTurn(Deck deck, List<Card> dealerHand) {
        System.out.println("Карты дилера: " + dealerHand + " > " + calculateHandValue(dealerHand));
        if (calculateHandValue(dealerHand) == winCount) {
            System.out.println("Блэкджек!");
            return GameResult.DEALER_WINS;
        }
        while (calculateHandValue(dealerHand) < maximumDealerClearance) {
            dealerHand.add(deck.drawCard());
            System.out.println("Дилер берёт карту...");
            System.out.println("Карты дилера: " + dealerHand
                    + " > " + calculateHandValue(dealerHand));
        }
        return GameResult.NO_ONE_WIN_YET;
    }

    /**
     * The function in which the winner is determined.
     */
    public static GameResult winChecking(List<Card> playerHand, List<Card> dealerHand) {
        int playerTotal = calculateHandValue(playerHand);
        int dealerTotal = calculateHandValue(dealerHand);

        if (dealerTotal > winCount || playerTotal > dealerTotal) {
            System.out.println("Вы выиграли раунд!");
            return GameResult.PLAYER_WINS;
        } else if (playerTotal == dealerTotal) {
            System.out.println("Ничья в раунде!");
            return GameResult.DRAW;
        } else {
            System.out.println("Дилер выиграл раунд.");
            return GameResult.DEALER_WINS;
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
        while (totalValue > winCount && aceCount > 0) {
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
