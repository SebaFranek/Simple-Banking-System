package banking;

import java.math.BigDecimal;
import java.sql.*;

class Database {

    private static volatile Database dbInstance = null;
    private Database() {
    }
    public static Database getInstance() {
        if (dbInstance == null) { //if there is no instance available... create new one
            dbInstance = new Database();
        }
        return dbInstance;
    }

    /**
     * * @param fileName is a database file name
     * * @param url is a database source path
     *
     */
    private String fileName = "card.s3db";
    private String url = "jdbc:sqlite:Simple Banking System\\task\\src\\banking\\" + fileName;

    /**
     * Connects to the database only for methods in this class
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);    // create a connection to the database
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Creates a file of new database
     *
     */
    /*
    void createDatabase() {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    */

    /**
     * Creates a new table "card" if it doesn't exist in the database
     *
     */
    void createTableCard() {

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "id INTEGER PRIMARY KEY ON CONFLICT REPLACE,\n"
                + "number TEXT,\n"
                + "pin TEXT,\n"
                + "balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts a new account into card table in database
     *
     * @param id is a id number of created card
     * @param number is a number of created card
     * @param pin is a pin of created card
     * @param balance is a balance of created card
     *
     */
    void insertNewAccount(int id, String number, String pin, BigDecimal balance) {

        String sql = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setBigDecimal(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts a income to the given card
     *
     * @param cardNumber is a id number of created card
     * @param income is a deposit amount
     *
     */

    void insertIncome(String cardNumber, BigDecimal income) {
        String sql = "UPDATE card\n" +
                "SET balance = balance + " + income + "\n" +
                "WHERE number = " + cardNumber + ";";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Transfers money between accounts in database
     ** @param cardNumber card from which funds are withdrawn
     ** @param recipient card to which funds are transferred
     ** @param amount the amount of funds to be transferred
     *
     */
    void transferMoney(String cardNumber, String recipient, BigDecimal amount){

        //withdrawing funds from the currently logged account
        String sql1 = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt1  = conn.prepareStatement(sql1)){
            pstmt1.setBigDecimal(1,amount);
            pstmt1.setString(2,cardNumber);

            pstmt1.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //transferring funds to the target account
        String sql2 = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt2  = conn.prepareStatement(sql2)){
            pstmt2.setBigDecimal(1,amount);
            pstmt2.setString(2,recipient);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks card number in database
     ** @param cardNumber card number to check
     *
     */
    int checkCardInDb(String cardNumber) {

        String sql = "SELECT EXISTS ("
                    + "SELECT number\n"
                    + "FROM card\n"
                    + "WHERE number = " + cardNumber + ")";

        int isExisting = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            isExisting = rs.getInt(1);



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isExisting;
    }

    /**
     * Checks the pin of given card.
     *
     * @param cardNumber  card number
     * @param pin pin to be checked
     */
    int checkCardPinInDb(String cardNumber, String pin) {

        String sql = "SELECT EXISTS ("
                + "SELECT pin\n"
                + "FROM card\n"
                + "WHERE number = " + cardNumber + " "
                + "\nAND pin = '" + pin + "')";

        int isExisting = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            isExisting = rs.getInt(1);



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isExisting;

    }

    /**
     * Checks the amount of money on the chosen card
     *
     * @param cardNumber  card number to be checked
     */
    long checkBalanceInDb(String cardNumber) {

        String sql = "SELECT balance\n"
                + "FROM card\n"
                + "WHERE number = " + cardNumber;

        String balance = "";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            balance = rs.getString("balance");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Long.parseLong(balance);

    }

    /**
     * Deletes a row (which is a card account) from the table.
     *
     * @param cardNumber card number to delete
     */
    void deleteAccount(String cardNumber) {

        String sql = "DELETE FROM card\n"
                + "WHERE number = " + cardNumber;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
