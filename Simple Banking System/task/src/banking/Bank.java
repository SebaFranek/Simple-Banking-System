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

    Service service = new Service();

    void session() {
        Scanner scanner = new Scanner(System.in);
        while (sessionOpen) {
            printMainMenu();
            switch (scanner.next()) {
                case "1":
                    service.createAccount();
                    break;
                case "2":
                    service.attemptLogin();
                    break;
                case "0":
                    System.out.println("\nBye!");
                    sessionOpen = false;
                    break;
                default:
                    System.out.println("\nWrong input! \nTry again!");
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