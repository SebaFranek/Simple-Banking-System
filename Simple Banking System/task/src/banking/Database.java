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
     * * @param fileName the database file name
     * * @param url the database source path
     *
     */
    private String fileName = Main.getFileName();
    private String url = "jdbc:sqlite:" + fileName;
    //private String url = "jdbc:sqlite:Simple Banking System\\task\\src\\banking\\" + fileName;

    /**
     * Connect to a database
     *
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

    //do usunięcia? Albo niech zostanie, będzie do sprawdzania połączenia przed logowaniem
    /**
     * Create a new database
     *
     */
    /*void createDatabase() {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    /**
     * Create a new table "card" in the test database
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

    void insertIncome(String insertedCard, BigDecimal income) {
        String sql = "UPDATE card\n" +
                "SET balance = balance + " + income + "\n" +
                "WHERE number = " + insertedCard + ";";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Transfer money between accounts in database
     ** @insertedCard card from which funds are withdrawn
     ** @recipient card to which funds are transferred
     ** @amount the amount of funds to be transferred
     *
     */
    void transferMoney(String insertedCard, String recipient, BigDecimal amount){

        //withdrawing funds from the currently logged account
        String sql1 = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt1  = conn.prepareStatement(sql1)){
            pstmt1.setBigDecimal(1,amount);
            pstmt1.setString(2,insertedCard);

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
     * Checking card number in database
     ** @cardNumber card number to check
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
     * @cardNumber  card number to check
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
