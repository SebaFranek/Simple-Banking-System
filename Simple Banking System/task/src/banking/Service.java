package banking;

import java.util.Scanner;

public class Service {

    private String insertedCard;
    private String insertedPin;


    void createAccount() {
        Account accountAccess = Account.getInstance();
        accountAccess.accountCreator();
        Database databaseAccess = Database.getInstance();
        databaseAccess.insertNewAccount(accountAccess.getAccountNo(),
                String.valueOf(accountAccess.getAccountCardNum()),
                String.valueOf(accountAccess.getAccountPin()),
                accountAccess.getAccountBalance());

    }

    void attemptLogin() {

        Card cardAccess = Card.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter your card number:");
        insertedCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        insertedPin = scanner.nextLine();

        if (!cardAccess.isCardInDb(insertedCard)) {                        // w przyszłości podzielić na numer i pin
            System.out.println("Wrong card number or PIN!\n");             // w przyszłości podzielić na numer i pin
        } else if (!cardAccess.isPinCorrect(insertedCard, insertedPin)) {  // w przyszłości podzielić na numer i pin
            System.out.println("Wrong card number or PIN!\n");             // w przyszłości podzielić na numer i pin
        } else {
            System.out.println("\nYou have successfully logged in!");
            Account accountAccess = Account.getInstance();
            accountAccess.insideAccount(insertedCard, insertedPin);
        }

    }
}