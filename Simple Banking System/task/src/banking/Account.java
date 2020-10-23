package banking;

import java.util.Random;
import java.util.Scanner;

import static banking.Bank.*;

class Account {

    private static volatile Account accInstance = null;

    private Account() {
    }

    private static int accountNo = 0;

    private static String accountCardNum;
    private static String accountPin;
    private static long accountBalance;

    public static String getAccountCardNum() {
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

    void insideAccount(String insertedCard, String insertedPin) {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = true;

        while (isSessionOpen() && loggedIn) {
            printInsideAccountMessage();
            switch (scanner.next()) {
                case "1":
                    long balance = checkBalance(insertedCard);
                    System.out.println("\nBalance: " + balance + "\n");
                    break;
                case "2":
                    addIncome(insertedCard);
                    break;
                case "3":
                    doTransfer(insertedCard);
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

    private long checkBalance(String cardNumber) {
        Database databaseAccess = Database.getInstance();
        String balance = databaseAccess.checkBalance(cardNumber);
        long balance2 = Long.parseLong(balance);
        return balance2;
    }

    private void addIncome(String insertedCard) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter income:");
        long income = scanner.nextLong();

        Database databaseAccess = Database.getInstance();
        databaseAccess.insertIncome(insertedCard, income);
    }

    private void doTransfer(String insertedCard) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String recipient = scanner.nextLine();

        boolean isCardCorrect = isCardCorrect(recipient);
        boolean doesCardExists = doesCardExists(recipient);


        if (!isCardCorrect) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        } else if (!doesCardExists) {
            System.out.println("Such a card does not exist.");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            long amount = scanner.nextLong();
            //isBalanceSufficient(recipient, amount); później jak połączysz balans z bazą danych

            if (checkBalance(insertedCard) < amount) {
                System.out.println("Not enough money!");
            } else{
                Database databaseAccess = Database.getInstance();
                databaseAccess.transferMoney(insertedCard, recipient, amount);
                System.out.println("Success!");
            }
        }
    }

    private boolean isCardCorrect(String cardNumber) {

        boolean correct = false;
        int answer = Card.checkLuhn(cardNumber);

        if (answer == 1) {
            correct = true;
        }
        return correct;
    }

    private boolean doesCardExists(String cardNumber) {
        Database databaseAccess = Database.getInstance();
        int answer = databaseAccess.checkCardInDb(cardNumber);
        boolean exists = false;
        if (answer == 1) {
            exists = true;
        }
        return exists;
    }

    private void isBalanceSufficient(String cardNumber, long amount) {
        if (checkBalance(cardNumber) < amount) {
            System.out.println("Not enough money!");
        }

        //finish it
    }

    private void closeAccount() {
    }

    private static String pinGenerator() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String pin = String.format("%04d", randomNum);
        return pin;
    }

    private static String cardGenerator() {

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

        return numInText;
    }

    private static void printNewAccountMessage(String cardNumber, String pin) {
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