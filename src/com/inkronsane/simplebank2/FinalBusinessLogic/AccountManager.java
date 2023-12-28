package com.inkronsane.simplebank2.FinalBusinessLogic;


import java.io.*;
import java.util.*;

public class AccountManager {

    private static final String ACCOUNTS_DIRECTORY = "accounts/";
    private static final String ACCOUNT_INFO = "account_info.txt";
    private static final String EUR = "EUR_balance.txt";
    private static final String USDT = "USDT_balance.txt";
    private static final String UAH = "UAH_balance.txt";
    private static final String CARDS = "cards.txt";

    public static boolean createAccount(String firstName, String lastName, int passportId,
      String password) throws IOException {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY + passportId);
        accountDirectory.mkdirs();
        File balanceFileUSDT = new File(accountDirectory, USDT);
        File balanceFileUAH = new File(accountDirectory, UAH);
        File balanceFileEUR = new File(accountDirectory, EUR);
        TextWriter.saveData(String.valueOf(0), balanceFileUSDT);
        TextWriter.saveData(String.valueOf(0), balanceFileUAH);
        TextWriter.saveData(String.valueOf(0), balanceFileEUR);
        File accountInfoFile = new File(accountDirectory, ACCOUNT_INFO);
        String accountInfo = "First Name: " + firstName + System.lineSeparator() +
          "Last Name: " + lastName + System.lineSeparator() +
          "PassportId: " + passportId + System.lineSeparator() +
          "Password: " + password;
        TextWriter.saveData(accountInfo, accountInfoFile);
        return true;
    }

    public static Account authenticate(int passportId) throws IOException {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY + passportId, ACCOUNT_INFO);
        List<String> accountInfoLines = TextRead.readAllLines(accountDirectory);
        String ufirstName = accountInfoLines.get(0).substring("First Name: ".length());
        String ulastName = accountInfoLines.get(1).substring("Last Name: ".length());
        int upassportId = Integer.parseInt(
          accountInfoLines.get(2).substring("PassportId: ".length()));
        String upassword = accountInfoLines.get(3).substring("Password: ".length());
        return new Account(ufirstName, ulastName, upassportId, upassword);
    }

    public static boolean authenticatePassword(int passportId, String password) throws IOException {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY + passportId, ACCOUNT_INFO);
        String accountInfo = TextRead.readAllText(accountDirectory);
        return accountInfo.contains("Password: " + password);
    }

    public static boolean accountExists(int passportId) {
        File accountDirectory = new File(ACCOUNTS_DIRECTORY + passportId);
        return accountDirectory.exists();
    }

    public static void addToBalance(Account account, String cardName, int amount)
      throws IOException {
        int currentBalance = getBalanceForCard(account, cardName);
        int newBalance = currentBalance + amount;
        TextWriter.saveData(String.valueOf(newBalance), getBalanceFile(account, cardName));
    }


    public static boolean subtractFromBalance(Account account, String cardName, int amount)
      throws IOException {
        int currentBalance = getBalanceForCard(account, cardName);

        if (currentBalance >= amount) {
            TextWriter.balanceDestroyer(getBalanceFile(account, cardName), currentBalance - amount);
            return true;  // Subtraction successful
        } else {
            return false;  // Insufficient funds
        }
    }




    public static int getBalanceForCard(Account account, String cardName) throws IOException {
        return TextRead.readBalance(getBalanceFile(account, cardName));
    }

    public static File getBalanceFile(Account account, String cardName) throws IOException {
        File cardsFile = new File(account.getCardsDirectory(), CARDS);

        try (BufferedReader reader = new BufferedReader(new FileReader(cardsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cardInfo = line.split(",");
                String currentCardName = cardInfo[1].trim();
                if (currentCardName.equalsIgnoreCase(cardName)) {
                    String fileName = cardInfo[2] + "_balance.txt";
                    return new File(account.getAccountDirectory(), fileName);
                }
            }
        }

        return null;
    }


    public static boolean createCard(Account account, String cardName, Currency currency)
      throws IOException {
        File cardsFile = new File(account.getCardsDirectory(), CARDS);
        Account.Card newCard = new Account.Card(cardName, currency.name());
        saveCardInfo(cardsFile, newCard);
        return true;
    }

    private static void saveCardInfo(File cardsFile, Account.Card card) throws IOException {
        BufferedWriter writer = new BufferedWriter(
          new FileWriter(cardsFile, true));// Додаємо до кінця файлу
        writer.write(card.getCardId() + "," + card.getCardName() + ","
          + card.getCurrency());// Запис інформації про нову карткy
        writer.newLine();
        writer.close();
    }


    public static class Account {

        public static String accountDirectory = null;
        private final String firstName;
        private final String lastName;
        private final int passportId;
        private final String password;

        public Account(String firstName, String lastName, int passportId, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.passportId = passportId;
            this.password = password;
            accountDirectory = ACCOUNTS_DIRECTORY + "/" + passportId;
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

        private String getCardsDirectory() {
            return accountDirectory;
        }



        private static class Card {

            private final int cardId;
            private final String currency;
            private final String cardName;

            public Card(String cardName, String currency) {
                this.cardId = randomId();
                this.cardName = cardName;
                this.currency = currency;
            }

            public String getCardName() {
                return cardName;
            }

            public String getCurrency() {
                return currency;
            }

            public int getCardId() {
                return cardId;
            }

            private int randomId() {
                Random random = new Random();
                return random.nextInt(10000000);
            }//Є шанс карток з однаковим айді, але це допустимо


        }
    }
}
