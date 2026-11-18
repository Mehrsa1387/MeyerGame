
package org.example;

import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);

    static String[] players = {"Spiller 1", "Spiller 2"};
    static int[] lives = {6, 6};

    // Rækkefølgen bestemmer, hvilket kast der er bedst.
    static int[] ranking = {
            21, 31,
            66, 65, 64, 63, 62, 61,
            55, 54, 53, 52, 51,
            44, 43, 42, 41,
            33, 22, 11
    };

    public static void main(String[] args) {

        int currentPlayer = 0;
        int previousPlayer = -1;
        int previousClaim = -1;
        int previousActual = -1;

        System.out.println("MEYER YAY");
        System.out.println("Begge spillere starter med 6 liv :)");

        // Spillet fortsætter indtil en af dem mister alle sine liv.
        while (lives[0] > 0 && lives[1] > 0) {

            System.out.println("\n-------------------------");
            System.out.println("Det er " + players[currentPlayer] + "'s tur.");
            System.out.println(players[0] + ": " + lives[0] + " liv");
            System.out.println(players[1] + ": " + lives[1] + " liv");
            System.out.println("-------------------------");

            // Hvis der allerede er blevet erklæret et resultat.
            if (previousPlayer != -1) {

                System.out.println(players[previousPlayer]
                        + " har erklæret: "
                        + getResultName(previousClaim));

                System.out.println("Tror du på det?");
                System.out.println("1. Ja");
                System.out.println("2. Nej");
                int choice = getChoice(1, 2);

                if (choice == 2) {
                    System.out.println("\nDet faktiske kast var: "
                            + getResultName(previousActual));

                    // Tjekker basically om den tidligere spiller har løjet
                    if (getRankValue(previousActual)
                            < getRankValue(previousClaim)) {

                        System.out.println(players[previousPlayer] + " bluffede");
                        loseLife(previousPlayer);

                    } else {
                        System.out.println(players[previousPlayer]
                                + " talte sandt");
                        loseLife(currentPlayer);
                    }

                    // Den udfordrende spiller starter en ny runde.
                    previousPlayer = -1;
                    previousClaim = -1;
                    previousActual = -1;
                }
            }

            // kaster terningerne.
            int[] dice = rollTurn();
            int actualResult = getDiceValue(dice[0], dice[1]);

            System.out.println("\nDu slog: " + dice[0] + " og " + dice[1]);
            System.out.println("Dit faktiske resultat er: " + getResultName(actualResult));

            // Spilleren skal erklære noget
            int minValue = 0;
            if (previousClaim != -1) {
                minValue = getRankValue(previousClaim);
            }
            int claimedResult = chooseClaim(minValue);

            // Gem både det faktiske og det erklærede resultat
            previousActual = actualResult;
            previousClaim = claimedResult;
            previousPlayer = currentPlayer;

            System.out.println("\nDu erklærede: "
                    + getResultName(claimedResult));

            // Skift til den anden spiller.
            currentPlayer = 1 - currentPlayer;
        }

        System.out.println("\nSPILLET ER SLUT. now leave me tf alone.");
        if (lives[0] <= 0) {
            System.out.println(players[1] + " har vundet!");
        } else {
            System.out.println(players[0] + " har vundet!");
        }

    }

    // Kaster en enkelt terning.
    public static int rollDice() {
        return (int) (Math.random() * 6 + 1);
    }

    // Kaster med begge terninger og giver mulighed for ét omkast.
    public static int[] rollTurn() {

        int num1 = rollDice();
        int num2 = rollDice();
        int actualResult = getDiceValue(num1, num2);

        System.out.println("Du slog: " + num1 + " og " + num2);
        System.out.println("Dit faktiske resultat er: " + getResultName(actualResult));
        System.out.println("Vil du kaste om? (Skriv tal)");
        System.out.println("1. Ja");
        System.out.println("2. Nej");
        int choice = getChoice(1, 2);

        if (choice == 1) {
            System.out.println("Du kaster om:");
            num1 = rollDice();
            num2 = rollDice();
            System.out.println("Nyt kast: " + num1 + " og " + num2);
        }
        return new int[]{num1, num2};
    }

    // Finder værdien af et terningekast
    public static int getDiceValue(int num1, int num2) {

        // Meyer
        if ((num1 == 1 && num2 == 2)
                || (num1 == 2 && num2 == 1)) {
            return 21;
        }

        // Lille Meyer
        if ((num1 == 1 && num2 == 3)
                || (num1 == 3 && num2 == 1)) {
            return 31;
        }

        // To ens
        if (num1 == num2) {
            return num1 * 11;
        }

        // Alle andre kast sorteres med det højeste tal først.
        int high = Math.max(num1, num2);
        int low = Math.min(num1, num2);

        return high * 10 + low;
    }

    // Finder rangeringen af et resultat.
    // Jo højere værdien er, jo bedre er kastet.
    public static int getRankValue(int result) {
        for (int i = 0; i < ranking.length; i++) {
            if (ranking[i] == result) {
                return ranking.length - i;
            }
        }
        return 0;
    }

    // Gør det muligt for spilleren at erklære et resultat
    public static int chooseClaim(int minValue) {
        int claim;
        while (true) {
            System.out.println("\nHvilket resultat vil du erklære?");
            System.out.println("Skriv fx 21 for Meyer, 31 for Lille Meyer.");
            System.out.println("Du kan også skrive fx 64, 53 eller 66.");
            claim = input.nextInt();

            int claimRank = getRankValue(claim);

            if (claimRank == 0) {
                System.out.println("Det er ikke et gyldigt resultat.");
            } else if (claimRank < minValue) {
                System.out.println("Du skal erklære mindst det samme "
                        + "som det tidligere resultat!");
            } else {
                return claim;
            }
        }
    }

    // Finder navnet på et terningekast.
    public static String getResultName(int result) {

        if (result == 21) {
            return "Meyer";
        }

        if (result == 31) {
            return "Lille Meyer";
        }

        if (result % 11 == 0) {
            return "Par " + (result / 11);
        }

        return "" + result;
    }

    // Fjerner et liv
    public static void loseLife(int player) {

        lives[player]--;

        System.out.println(players[player] + " mister et liv!");

        System.out.println("Liv tilbage: " + lives[player]);
    }

    // Sikrer at spilleren vælger en gyldig mulighed (fejl check)
    public static int getChoice(int min, int max) {
        int choice;
        while (true) {
            choice = input.nextInt();
            if (choice >= min && choice <= max) {
                return choice;
            }
            System.out.println("Ugyldigt valg. Prøv igen.");
        }
    }
}
