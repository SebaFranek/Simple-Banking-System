package banking;

import java.math.BigDecimal;
import java.util.Scanner;

import static banking.Bank.*;

class Account {

    private static volatile Account accInstance = null;

    private Account() {
    }

    private int accountNo = 0;
    private String accountCardNum;
    private String accountPin;
    private BigDecimal accountBalance;

    public String getAccountCardNum() {
        return accountCardNum;
    }
    public String getAccountPin() {
        return accountPin;
    }
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
    public int getAccountNo() {
        return accountNo;
    }      // czy trzeba generować nowy numer id?
                                                                // + sprawdzanie w bazie czy istnieje
    public static Account getInstance() {
        if (accInstance == null) { //if there is no instance available... create new one
            accInstance = new Account();
        }
        return accInstance;
    }

    Card cardAccess = Card.getInstance();

    void accountCreator() {

        accountCardNum = cardAccess.cardGenerator();
        accountPin = cardAccess.pinGenerator();
        accountBalance = new BigDecimal(0);
        accountNo++;
        printNewAccountMessage(accountCardNum, accountPin);
    }

    void insideAccount(String insertedCard, String insertedPin) {

        accountCardNum = insertedCard;
        accountPin = insertedPin;

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = true;

        while (isSessionOpen() && loggedIn) {
            printInsideAccountMessage();
            switch (scanner.next()) {
                case "1":
                    System.out.println("\nBalance: " + checkBalance(insertedCard) + "\n");
                    break;
                case "2":
                    addIncome(insertedCard);
                    break;
                case "3":
                    doTransfer(insertedCard);
                    break;
                case "4":
                    closeAccount(insertedCard);
                    loggedIn = false;
                    break;
                case "5":
                    System.out.println("\nYou have successfully logged out!");
                    loggedIn = false;
                    break;
                case "0":
                    System.out.println("\nBye!");
                    setSessionOpen(false);
                    break;
                default:
                    System.out.println("\nWrong input! \nTry again!");
                    break;
            }
        }
    }

    private BigDecimal checkBalance(String cardNumber) {
        Database databaseAccess = Database.getInstance();
        BigDecimal balance = new BigDecimal(databaseAccess.checkBalanceInDb(cardNumber));
        return balance;
    }

    private void addIncome(String insertedCard) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter income:");
        BigDecimal income = new BigDecimal(scanner.nextLong());

        Database databaseAccess = Database.getInstance();
        databaseAccess.insertIncome(insertedCard, income);

        System.out.println("Income was added!");
    }

    private void doTransfer(String insertedCard) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String recipient = scanner.nextLine();

        boolean isCardCorrect = cardAccess.isCardCorrect(recipient);
        boolean doesCardExists = cardAccess.isCardInDb(recipient);

        if(accountCardNum.equals(recipient)){
            System.out.println("You can't transfer money to the same account!");
        }else if (!isCardCorrect) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        } else if (!doesCardExists) {
            System.out.println("Such a card does not exist.");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            BigDecimal amount = new BigDecimal(scanner.nextLong());

            if (!isBalanceSufficient(accountCardNum, amount)) {
                System.out.println("Not enough money!");
            } else {
                Database databaseAccess = Database.getInstance();
                databaseAccess.transferMoney(insertedCard, recipient, amount);
                System.out.println("Success!");
            }
        }
    }

    private boolean isBalanceSufficient(String cardNumber, BigDecimal amount) {
        boolean sufficient = false;
        Database databaseAccess = Database.getInstance();
        BigDecimal balanceInDb = new BigDecimal(databaseAccess.checkBalanceInDb(cardNumber));
        if (balanceInDb.compareTo(amount) >= 0) {
            sufficient = true;
        }
        return sufficient;
    }

    private void closeAccount(String insertedCard) {
        Database databaseAccess = Database.getInstance();
        databaseAccess.deleteAccount(insertedCard);
        System.out.println("\nThe account has been closed!");
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