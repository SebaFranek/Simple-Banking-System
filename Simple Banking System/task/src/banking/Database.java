package banking;

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

    /**
     * Create a new database
     *
     */
    void createDatabase() {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    void insertNewAccount(int id, String number, String pin, long balance) {

        String sql = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setLong(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks the amount of money on the chosen card
     *
     * @return
     */
    String checkBalance(String cardNumber){

        String sql = "SELECT balance FROM card WHERE number = " + cardNumber;
        String balance = "";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            balance = rs.getString("balance");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    void insertIncome(String insertedCard, long income) {
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
     ** @param insertedCard card from which funds are withdrawn
     ** @param recipient card to which funds are transferred
     ** @param amount the amount of funds to be transferred
     *
     */
    void transferMoney(String insertedCard, String recipient, long amount){

        //withdrawing funds from the currently logged account
        String sql1 = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt1  = conn.prepareStatement(sql1)){
            pstmt1.setLong(1,amount);
            pstmt1.setString(2,insertedCard);

            pstmt1.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //transferring funds to the target account
        String sql2 = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt2  = conn.prepareStatement(sql2)){
            pstmt2.setLong(1,amount);
            pstmt2.setString(2,recipient);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

}
