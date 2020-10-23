package banking;

public class Card {

    static int checkLuhn(String cardNumber) {

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
            answer = 1;
        } else {
            answer = 0;
        }

        return answer;
    }
}
