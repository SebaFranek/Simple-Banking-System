package banking;

public class Main {

    public static void main(String[] args) {
        Database databaseAccess = Database.getInstance();
        databaseAccess.createTableCard();

        Bank bank = new Bank();
        bank.session();
    }
}
