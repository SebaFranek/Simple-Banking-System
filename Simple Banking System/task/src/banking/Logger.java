
package banking;

import java.util.Scanner;

import static banking.Account.*;

class Logger {

    private String insertedCard;
    private String insertedPin;

    void attemptLogin() {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("Enter your card number:");
        insertedCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        insertedPin = scanner.nextLine();

        //Database databaseAccess = Database.getInstance();
        //databaseAccess.checkCard(insertedCard);

        //robotka na później - porównywnaie z ostatnią kartą z bazy danych
        //aktualnie porównuje ze zmienną deklarowaną przy tworzeniu konta
        //poniżej


        if (!insertedCard.equals(getAccountCardNum()) || !insertedPin.equals(getAccountPin())) {
            System.out.println("Wrong card number or PIN!");
            System.out.println();
        } else {
            System.out.println("You have successfully logged in!");
            System.out.println();
            Account accountAccess = Account.getInstance();
            accountAccess.insideAccount(insertedCard, insertedPin);
        }
    }
}

