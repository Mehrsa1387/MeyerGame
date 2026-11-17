package org.example;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean hasRolledAgain = false;

        int num1 = RollDice();
        int num2 = RollDice();
        System.out.println(num1+" and "+ num2);
        System.out.println("roll again? (y/n)");
        String rollagain = input.nextLine();
        if (rollagain.equalsIgnoreCase("y")) {
            hasRolledAgain = true;
            num1 = RollDice();
            num2 = RollDice();
            System.out.println(num1+" and "+ num2);
        }

    }
    public static int RollDice(){
        return (int) (Math.random()*6+1);
    }
}