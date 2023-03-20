package banking;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String cardNumber;
    private String pin;
    private int balance;
    private static Map<String, User> cardNumberPinMap = new HashMap<>();

    public User(int balance) {
        this.cardNumber = generateCardNumber();
        this.pin = generatePin();
        this.balance = balance;
        cardNumberPinMap.put(cardNumber, this);
    }

    public static Map<String, User> getCardNumberPinMap() {
        return cardNumberPinMap;
    }

    public static void setCardNumberPinMap(Map<String, User> cardNumberPinMap) {
        User.cardNumberPinMap = cardNumberPinMap;
    }

    public User(String pin, int balance) {
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance += balance;
    }

    private static String generateCardNumber() {
        String cardNumber = "400000";
        for (int i = 0; i < 9; i++) {
            cardNumber += (int) (Math.random() * 10);
        }
        cardNumber += generateCheckSum(cardNumber);
        return cardNumber;
    }

    private static String generatePin() {
        String pin = "";
        for (int i = 0; i < 4; i++) {
            pin += (int) (Math.random() * 10);
        }
        return pin;
    }

    private static int generateCheckSum(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        return (10 - sum % 10) % 10;
    }

    public static boolean isCardNumberValid(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        return sum % 10 == 0;
    }
}