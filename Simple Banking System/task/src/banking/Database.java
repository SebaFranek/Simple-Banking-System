package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

class Database {


    private static volatile Database databaseInstance = null;
    private Database(){};

    public static Database getInstance() {
        if (databaseInstance == null) { //if there is no instance available... create new one
            databaseInstance = new Database();
        }
        return databaseInstance;
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

    void insertData(int id, String number, String pin, long balance){

        String sql = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setLong(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

     /*void dropTable(){
        String sql = "DROP TABLE card";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/
}
