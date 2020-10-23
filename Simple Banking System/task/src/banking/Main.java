package banking;

public class Main {

    private static String fileName;
    public static String getFileName() {
        return fileName;
    }
    public static void main(String[] args) {

        args = new String[]{"-fileName", "card.s3db"};

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-fileName":
                    fileName = args[i + 1];
                    break;
                default:
                    break;
            }
        }

        Database databaseAccess = Database.getInstance();
        //databaseAccess.createDatabase();   //do usunięcia? będzie sprawdzało połączenie
        databaseAccess.createTableCard();

        Bank bank = new Bank();
        bank.session();

    }
}
