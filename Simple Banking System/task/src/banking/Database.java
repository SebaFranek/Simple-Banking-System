package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

class Database {


    private static volatile Database dbInstance = null;
    private Database(){}

    public static Database getInstance() {
        if (dbInstance == null) { //if there is no instance available... create new one
            dbInstance = new Database();
        }
        return dbInstance;
    }


    private SQLiteDataSource dataSource = new SQLiteDataSource();
    private String fileName = Main.getFileName();
    private String url = "jdbc:sqlite:" + fileName;
    //private String url = "jdbc:sqlite:Simple Banking System\\task\\src\\banking\\" + fileName;

    void createDatabase(){

        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createTableCard(){

        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertNewAccount(int id, String number, String pin, long balance){

        String sql = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, number);
            statement.setString(3, pin);
            statement.setLong(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void addIncome(long insertedCard, long income){
        String sql = "UPDATE card\n" +
                "SET balance = balance + " + income + "\n" +
                "WHERE number = " + insertedCard + ";";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void checkBalance(long insertedCard){
        String sql = "SELECT balance FROM card";
        //String sql = "SELECT balance FROM card WHERE number =" + insertedCard + ";";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql);
             ResultSet result = statement.executeQuery(sql)){
            while (result.next()) {
                System.out.println(result.getLong("balance") +  "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
