package banking;

import java.util.Scanner;

import static banking.Account.*;

class Bank {

    private static boolean sessionOpen = true;
    static boolean isSessionOpen() {
        return sessionOpen;
    }
    static void setSessionOpen(boolean status) {
        sessionOpen = status;
    }

    void session() {
        Scanner scanner = new Scanner(System.in);
        while (sessionOpen) {
            printMainMenu();
            switch (scanner.next()) {
                case "1":
                    Account accountAccess = Account.getInstance();
                    accountAccess.accountCreator();
                    Database databaseAccess = Database.getInstance();
                    databaseAccess.insertNewAccount(getAccountNo(), String.valueOf(getAccountCardNum()), String.valueOf(getAccountPin()), getAccountBalance());
                    break;
                case "2":
                    Logger logger = new Logger();
                    logger.attemptLogin();
                    break;
                case "0":
                    System.out.println("\nBye!");
                    sessionOpen = false;
                    break;
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        System.out.println();
    }
}