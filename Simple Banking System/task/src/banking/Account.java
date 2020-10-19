package banking;

import java.util.Random;
import java.util.Scanner;

import static banking.Bank.*;


class Account {
    private static Account[] clientAccount = new Account[1000];
    private static int accountNo = 0;

    private static long accountCardNum;
    private static String accountPin;
    private static long accountBalance;

    public static long getAccountCardNum() {
        return accountCardNum;
    }

    public static String getAccountPin() {
        return accountPin;
    }

    public static long getAccountBalance() {
        return accountBalance;
    }


    Account(){

    }

    Account(long accountCardNum, String accountPin) {
        this.accountCardNum = accountCardNum;
        this.accountPin = accountPin;
        this.accountBalance = 0L;
    }


    static void accountCreator() {

        long cardNum = cardGenerator();
        String pin = pinGenerator();

        clientAccount[accountNo++] = new Account(cardNum, pin);
        printNewAccountMessage(accountCardNum, accountPin);

    }

    private static String pinGenerator() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String pin = String.format("%04d", randomNum);
        return pin;
    }

    private static long cardGenerator() {

        final int[] BIN = new int[] {4, 0, 0, 0, 0, 0};
        final int[] accountIdentifier = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final int[] cardNumArr = new int[BIN.length + accountIdentifier.length];

        //account identifier creating
        for (int i = 0; i <= accountIdentifier.length - 1; i++) {
            Random random = new Random();
            int number = random.nextInt(10);
            accountIdentifier[i] = number;
        }

        System.arraycopy(BIN, 0, cardNumArr, 0, BIN.length);
        System.arraycopy(accountIdentifier, 0, cardNumArr, BIN.length, accountIdentifier.length);

        int sum = 0;
        int checksum;
        for (int i = 0; i < cardNumArr.length - 1; i++) {
            int product;
            if (i % 2 != 0) {
                product = cardNumArr[i] * 1;
            } else {
                product = cardNumArr[i] * 2;
            }
            if (product > 9)
                product -= 9;
            sum += product;
        }

        checksum = cardNumArr[15];

        for (int i = 0; i <= 10; i++) {
            if ((sum + checksum) % 10 != 0) {
                checksum = i;
            } else {
                break;
            }
        }

        cardNumArr[15] = checksum;

        StringBuilder builder = new StringBuilder();
        for (int value : cardNumArr) {
            builder.append(value);
        }
        String numInText = builder.toString();
        long numInLong = Long.parseLong(numInText);

        return numInLong;
    }

    void insideAccount() {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = true;

        while (isSessionOpen() && loggedIn) {
            printInsideAccountMessage();
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("\nBalance: " + getAccountBalance() + "\n");
                    break;
                case 2:
                    System.out.println("\nYou have successfully logged out!\n");
                    loggedIn = false;
                    break;
                case 0:
                    System.out.println("\nBye!");
                    setSessionOpen(false);
                    break;
            }
        }
    }

    private static void printNewAccountMessage(long cardNumber, String pin) {
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
    }

    private static void printInsideAccountMessage() {
        System.out.println();
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
        System.out.println();
    }
}