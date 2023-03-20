package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Database dbCon;
    private Scanner scanner = new Scanner(System.in);
    private String currentAccount;

    public AccountManager(String fileName) {
        dbCon = new Database(fileName);
    }

    public void run() {
        while (true) {
            System.out.println(Menu.MAIN_MENU);
            switch (scanner.nextInt()) {
                case 1 -> createAccount();
                case 2 -> logIntoAccount();
                case 0 -> exit();
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public void createAccount() {
        User user = new User(0);
        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + user.getCardNumber());
        System.out.println("Your card PIN:\n" + user.getPin());
        dbCon.insertUserToDB(user.getCardNumber(), user.getPin());
    }

    public void logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        if (User.getCardNumberPinMap().containsKey(cardNumber) && User.getCardNumberPinMap().get(cardNumber).getPin().equals(pin)) {
            System.out.println("You have successfully logged in!");
            currentAccount = cardNumber;
            while (true) {
                System.out.println(Menu.ACCOUNT_MENU);
                switch (scanner.nextInt()) {
                    case 1 -> checkBalance();
                    case 2 -> addIncome();
                    case 3 -> doTransfer();
                    case 4 -> {
                        closeAccount();
                        return;
                    }
                    case 5 -> {
                        logout();
                        return;
                    }
                    case 0 -> exit();
                    default -> System.out.println("Invalid choice, please try again!");
                }
            }

        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    public void addIncome() {
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        dbCon.updateBalance(currentAccount, income);
        User.getCardNumberPinMap().get(currentAccount).setBalance(User.getCardNumberPinMap().get(currentAccount).getBalance() + income);
        System.out.println("Income was added!");
    }

    public void doTransfer() {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        scanner.nextLine();
        String cardNumber = scanner.nextLine();

        if (User.isCardNumberValid(cardNumber)) {
            if (User.getCardNumberPinMap().containsKey(cardNumber)) {
                System.out.println("Enter how much money you want to transfer:");
                int amount = scanner.nextInt();
                if (amount > (User.getCardNumberPinMap().get(currentAccount).getBalance())) {
                    System.out.println("Not enough money!");
                } else {
                    User.getCardNumberPinMap().get(currentAccount).setBalance(User.getCardNumberPinMap().get(currentAccount).getBalance() - amount);
                    User.getCardNumberPinMap().get(cardNumber).setBalance(User.getCardNumberPinMap().get(cardNumber).getBalance() + amount);
                    dbCon.updateBalance(currentAccount, -amount);
                    dbCon.updateBalance(cardNumber, amount);
                    System.out.println("Success!");
                }
            } else {
                System.out.println("Such a card does not exist.");
            }
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }
    }

    private void closeAccount() {
        dbCon.deleteUserFromDB(currentAccount);
        User.getCardNumberPinMap().remove(currentAccount);
        System.out.println("The account has been closed!");
    }

    private void exit() {
        System.out.println("Bye!");
        try {
            dbCon.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    private boolean logout() {
        System.out.println("You have successfully logged out!");
        return false;
    }

    private void checkBalance() {
        System.out.println("Balance: " + User.getCardNumberPinMap().get(currentAccount).getBalance());
    }
}