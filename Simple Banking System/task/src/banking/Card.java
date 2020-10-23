package banking;

import java.util.Random;

public class Card {

    private static volatile Card cardInstance = null;

    private Card() {

    }

    public static Card getInstance() {
        if (cardInstance == null) {
            cardInstance = new Card();
        }
        return cardInstance;
    }

    Database databaseAccess = Database.getInstance();

    String cardGenerator() {

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

    String pinGenerator() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String pin = String.format("%04d", randomNum);
        return pin;
    }

    boolean isCardCorrect(String cardNumber) {

        boolean correct = false;

        int answer;
        int[] ints = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            ints[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
        }

        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }

        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }

        if (sum % 10 == 0) {
            correct = true;
        }

        return correct;
    }

    boolean isCardInDb(String cardNumber) {

        boolean exists = false;
        int answer = databaseAccess.checkCardInDb(cardNumber);

        if (answer == 1) {
            exists = true;
        }
        return exists;
    }

    boolean isPinCorrect(String cardNumber, String pin) {

        boolean correct = false;
        int answer = databaseAccess.checkCardPinInDb(cardNumber, pin);

        if (answer == 1) {
            correct = true;
        }
        return correct;
    }
}
