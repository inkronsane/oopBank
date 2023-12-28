package com.inkronsane.bank.bl;


import com.inkronsane.bank.data.Currency;
import com.inkronsane.bank.data.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class BankSystem {

    public static final String CARD_CURRENCY = "Валюта: ";
    private static final String ACCOUNTS_DIRECTORY = "accounts";
    private static final String FIRST_NAME = "First Name: ";
    private static final String LAST_NAME = "Last Name: ";
    private static final String PASSPORT_ID = "Passport Id: ";
    private static final String PASSWORD = "Password: ";
    private static final String EUR = "EUR_balance.txt";
    private static final String USDT = "USDT_balance.txt";
    private static final String BALANCE = "_balance.txt";
    private static final String UAH = "UAH_balance.txt";
    private static final String CARDS = "cards";
    private static final String ACCOUNT_INFO = "account_info.txt";
    private static final String CARD_NAME = "Ім'я картки: ";
    private static final String LOGS = "logs.txt";

    public static void logWrite(Account account, String cardName, Goods selectedGoods,
      int quantity) throws IOException {

        Path filePath = Paths.get(account.getAccountDirectory(), LOGS);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        Files.write(
          filePath,
          List.of(
            "Фамілія покупця: " + account.getLastName() + System.lineSeparator() +
              "Ім'я картки: " + cardName + System.lineSeparator() +
              "Назва продукту: " + selectedGoods.getName() + System.lineSeparator() +
              "Ціна продукту: " + selectedGoods.getPrice() + System.lineSeparator() +
              "Кількість: " + quantity + System.lineSeparator() +
              "Ціна покупки " + selectedGoods.getPrice() * quantity + System.lineSeparator() +
              "Валюта: " + selectedGoods.getCurrency() + System.lineSeparator() +
              "------------------------------------"),
          StandardOpenOption.APPEND
        );

    }

    public static boolean createAccount(String firstName, String lastName, int passportId,
      String password) throws IOException {
        Account account = new Account(firstName, lastName, passportId, password);
        new File(account.getAccountDirectory()).mkdirs();
        new File(account.getCardsDirectory()).mkdirs();
        String balanceFileUSDT = new File(account.getAccountDirectory(), USDT).getPath();
        String balanceFileUAH = new File(account.getAccountDirectory(), UAH).getPath();
        String balanceFileEUR = new File(account.getAccountDirectory(), EUR).getPath();
        FileEditor.fileWrite(balanceFileUSDT, List.of(String.valueOf(0)));
        FileEditor.fileWrite(balanceFileUAH, List.of(String.valueOf(0)));
        FileEditor.fileWrite(balanceFileEUR, List.of(String.valueOf(0)));
        String accountInfoFile = new File(account.getAccountDirectory(), ACCOUNT_INFO).getPath();
        FileEditor.fileWrite(accountInfoFile, List.of(
          "First Name: " + firstName + System.lineSeparator() +
            "Last Name: " + lastName + System.lineSeparator() +
            "Passport Id: " + passportId + System.lineSeparator() +
            "Password: " + password
        ));
        return true;
    }

    public static Account authenticate(int passportId) throws IOException {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY, passportId + "/" + ACCOUNT_INFO);
        return new Account(FileEditor.findInFile(accountDirectory.getPath(), FIRST_NAME),
          FileEditor.findInFile(accountDirectory.getPath(), LAST_NAME),
          Integer.parseInt(FileEditor.findInFile(accountDirectory.getPath(), PASSPORT_ID)),
          FileEditor.findInFile(accountDirectory.getPath(), PASSWORD));
    }

    public static boolean authenticatePassword(int passportId, String password) throws IOException {
        String accountDirectory = new File(
          ACCOUNTS_DIRECTORY, passportId + "/" + ACCOUNT_INFO).getPath();
        String accountPassword = FileEditor.findInFile(accountDirectory, PASSWORD);
        return accountPassword.equalsIgnoreCase(password);
    }

    public static boolean accountExists(int passportId) {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY + passportId);
        return accountDirectory.exists();
    }

    public static int getBalanceForCard(Account account, String cardName) throws IOException {
        List<String> list = FileEditor.fileRead(
          account.getAccountDirectory() + "/" + (FileEditor.findInFile(
            account.getCardsDirectory() + "/" + cardName + ".txt",
            CARD_CURRENCY)) + BALANCE);
        return Integer.parseInt(list.get(0));
    }

    public static boolean addToBalance(Account account, String cardName, int amount)
      throws IOException {
        if (amount > 0) {
            int newBalance = getBalanceForCard(account, cardName) + amount;
            FileEditor.fileWrite(
              account.getAccountDirectory() + "/" + FileEditor.findInFile(
                account.getCardsDirectory() + "/" + cardName + ".txt", CARD_CURRENCY) + BALANCE,
              List.of(String.valueOf(newBalance)));
            return true;
        }
        return false;
    }

    public static boolean subtractToBalance(Account account, String cardName, int amount)
      throws IOException {
        int newBalance = getBalanceForCard(account, cardName) - amount;
        if (newBalance >= 0 && amount > 0) {
            FileEditor.fileWrite(
              account.getAccountDirectory() + "/" + FileEditor.findInFile(
                account.getCardsDirectory() + "/" + cardName + ".txt", CARD_CURRENCY) + BALANCE,
              List.of(String.valueOf(newBalance)));
            return true;
        }
        return false;
    }

    public static boolean createCard(Account account, String cardName,
      com.inkronsane.bank.data.Currency currency)
      throws IOException {
        boolean what = new File(account.getCardsDirectory() + "/" + cardName + ".txt").exists();
        if (!what) {
            Account.Card newCard = new Account.Card(account, cardName,
              currency);
            FileEditor.fileWrite(account.getCardsDirectory() + "/" + cardName + ".txt",
              List.of(newCard.toString()));
            return true;
        }
        return false;
    }

    public static void displayAccountInformation(Account account) throws IOException {
        String accountInfoPath = account.getAccountDirectory() + "/" + ACCOUNT_INFO;
        List<String> accountInfo = FileEditor.fileRead(accountInfoPath);
        System.out.println("Account Information:");
        for (String line : accountInfo) {
            System.out.println(line);
        }
    }

    public static void displayCardInformation(Account account, String cardName) throws IOException {
        String cardPath = account.getCardsDirectory() + "/" + cardName + ".txt";
        List<String> cardInfo = FileEditor.fileRead(cardPath);
        System.out.println("Card Information:");
        for (String line : cardInfo) {
            System.out.println(line);
        }
    }

    public static void displayLogs(Account account) throws IOException {
        String logsPath = account.getAccountDirectory() + "/" + LOGS;
        List<String> logs = FileEditor.fileRead(logsPath);
        System.out.println("Logs:");
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public static List<String> listFilesInDirectory(Account account) {
        File directory = new File(account.getCardsDirectory());

        return Arrays.stream(directory.listFiles(File::isFile))
          .map(File::getName)
          .map(name -> name.endsWith(".txt") ? name.substring(0, name.lastIndexOf(".")) : name)
          .collect(Collectors.toList());//K P A C O T A
    }

    public static class Account {

        public static String accountDirectory = null;
        public static String cardsDirectory = null;
        private final String firstName;
        private final String lastName;
        private final int passportId;
        private final String password;

        public Account(String firstName, String lastName, int passportId, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.passportId = passportId;
            this.password = password;
            accountDirectory = new File(ACCOUNTS_DIRECTORY + "/" + passportId + "/").toString();
            cardsDirectory = new File(accountDirectory + "/" + CARDS + "/").toString();
        }

        public String getFirstName() {
            return firstName;
        }

        public String getPassword() {
            return password;
        }

        public int getPassportId() {
            return passportId;
        }

        public String getLastName() {
            return lastName;
        }

        private String getAccountDirectory() {
            return accountDirectory;
        }

        String getCardsDirectory() {
            return cardsDirectory;
        }


        private static class Card {

            private final int accountId;
            private final int cardId;
            private final com.inkronsane.bank.data.Currency currency;
            private final String cardName;

            public Card(Account accountId, String cardName,
              com.inkronsane.bank.data.Currency currency) {
                this.accountId = accountId.passportId;//чисто формальність
                this.cardId = randomId();
                this.cardName = cardName;
                this.currency = currency;
            }

            public String getCardName() {
                return cardName;
            }

            public Currency getCurrency() {
                return currency;
            }

            public int getCardId() {
                return cardId;
            }

            private int randomId() {
                Random random = new Random();
                return random.nextInt(10000000);
            }//Є шанс карток з однаковим айді, але це допустимо

            @Override
            public String toString() {
                return "Айді аккаунту: " + accountId + System.lineSeparator() +
                  "Ім'я картки: " + cardName + System.lineSeparator() +
                  "Айді картки: " + cardId + System.lineSeparator() +
                  "Валюта: " + currency + System.lineSeparator();
            }
        }
    }
}
