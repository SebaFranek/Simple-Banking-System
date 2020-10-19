
package banking;

import java.util.Scanner;

import static banking.Account.*;

class Logger {

    private Long insertedCard;
    private String insertedPin;

    void attemptLogin() {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("Enter your card number:");
        insertedCard = scanner.nextLong();
        System.out.println("Enter your PIN:");
        insertedPin = scanner.next();

        if (!insertedCard.equals(getAccountCardNum()) || !insertedPin.equals(getAccountPin())) {
            System.out.println("Wrong card number or PIN!");
            System.out.println();
        } else {
            System.out.println("You have successfully logged in!");
            System.out.println();
            Account account1 = new Account();
            account1.insideAccount();
        }
    }
}

