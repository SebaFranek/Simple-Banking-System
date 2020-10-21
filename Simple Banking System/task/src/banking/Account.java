package banking;

import java.util.Random;
import java.util.Scanner;

import static banking.Bank.*;

class Account {

    private static volatile Account accInstance = null;
    private Account() { }

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

    public static int getAccountNo() {
        return accountNo;
    }

    public static Account getInstance() {
        if (accInstance == null) { //if there is no instance available... create new one
            accInstance = new Account();
        }
        return accInstance;
    }

    void accountCreator() {

        accountCardNum = cardGenerator();
        accountPin = pinGenerator();
        accountBalance = 0L;
        accountNo++;
        printNewAccountMessage(accountCardNum, accountPin);
    }

    void insideAccount(long insertedCard, String insertedPin) {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = true;

        while (isSessionOpen() && loggedIn) {
            printInsideAccountMessage();
            switch (scanner.next()) {
                case "1":
                    checkBalance(insertedCard);
                    break;
                case "2":
                    addIncome(insertedCard);
                    break;
                case "3":
                    doTransfer();
                    break;
                case "4":
                    closeAccount();
                    break;
                case "5":
                    System.out.println("\nYou have successfully logged out!\n");
                    loggedIn = false;
                    break;
                case "0":
                    System.out.println("\nBye!");
                    setSessionOpen(false);
                    break;
            }
        }
    }

    private void checkBalance(long insertedCard) {
        Database databaseAccess = Database.getInstance();
        databaseAccess.checkBalance(insertedCard);

        //System.out.println("\nBalance: " + getAccountBalance() + "\n");
    }

    private void addIncome(long insertedCard) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter income:");
        long income = scanner.nextLong();

        Database databaseAccess = Database.getInstance();
        databaseAccess.addIncome(insertedCard, income);
    }

    private void closeAccount() {
    }

    private void doTransfer() {
    }

    private static String pinGenerator() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String pin = String.format("%04d", randomNum);
        return pin;
    }

    private static long cardGenerator() {

        final int[] BIN = new int[]{4, 0, 0, 0, 0, 0};
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
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        System.out.println();
    }




}