package banking;

public enum Menu {
    MAIN_MENU("1. Create an account\n" +
            "2. Log into account\n" +
            "0. Exit"),
    ACCOUNT_MENU("1. Balance\n" +
            "2. Add income\n" +
            "3. Do transfer\n" +
            "4. Close account\n" +
            "5. Log out\n" +
            "0. Exit");

    private final String text;

    Menu(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
