package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        String fileName = getFileNameFromArgs(args);
        if (fileName == null) {
            System.out.println("Please provide the name of the database file using the -fileName argument");
            return;
        }

        AccountManager app = new AccountManager(fileName);
        User.setCardNumberPinMap(loadUsersFromDatabase(fileName));
        System.out.println("Users loaded from database: " + User.getCardNumberPinMap().size());
        System.out.println("Welcome to the banking app!");
        app.run();
    }

    private static String getFileNameFromArgs(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-fileName")) {
                return args[i + 1];
            }
        }
        return null;
    }

    private static HashMap<String, User> loadUsersFromDatabase(String fileName) {
        HashMap<String, User> users = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fileName)) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM card");
            while (resultSet.next()) {
                String cardNumber = resultSet.getString("number");
                String pin = resultSet.getString("pin");
                int balance = resultSet.getInt("balance");
                User user = new User(pin, balance);
                users.put(cardNumber, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}