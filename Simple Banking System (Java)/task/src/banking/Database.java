package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    public Database(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        dataSource.setUrl(url);
        createTable();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void createTable() {
        String createSql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createSql);
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertUserToDB(String cardNumber, String pin) {
        String insertSql = "INSERT INTO card (number, pin) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(String cardNumber, int income) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserFromDB(String cardNumber) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}