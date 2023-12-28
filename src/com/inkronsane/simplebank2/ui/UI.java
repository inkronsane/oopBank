package com.inkronsane.simplebank2.ui;


import com.inkronsane.simplebank2.FinalBusinessLogic.Currency;
import com.inkronsane.simplebank2.FinalBusinessLogic.*;
import java.io.*;
import java.util.*;

public class UI {

    private static final Scanner scanner = new Scanner(System.in);
    private static AccountManager.Account ACCOUNT;

    public static void startMenu() {
        int choice;
        do {
            printStartMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> registerAccount();
                case 2 -> authorization();
                case 3 -> System.exit(0); // Exit the application
                default -> System.out.println("Невірний вибір");
            }
        } while (choice != 0);
    }

    public static void printStartMenu() {
        System.out.println("===== Simple Bank App =====");
        System.out.println("1. Register an Account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void registerAccount() {
        System.out.println("===== Register an Account =====");
        System.out.print("Enter First Name: ");
        String firstName = scanner.next();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();
        System.out.print("Enter Passport ID: ");
        int passportId = scanner.nextInt();
        System.out.print("Enter Password: ");
        String password = scanner.next();
        try {
            if (!AccountManager.accountExists(passportId)) {
                AccountManager.createAccount(firstName, lastName, passportId, password);
                System.out.println("Account successfully created!");
            } else {
                System.out.println("Account alrweady");
            }
        } catch (IOException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    public static void authorization() {
        System.out.println("===== Login =====");
        System.out.println("Уведіть айді вашого паспорту: ");
        int passportId = scanner.nextInt();
        try {
            if (AccountManager.accountExists(passportId)) {
                System.out.println("Уведіть пароль: ");
                String password = scanner.next();
                if (AccountManager.authenticatePassword(passportId, password)) {
                    ACCOUNT = AccountManager.authenticate(passportId);
                    loggedInMenu();
                } else {
                    System.out.println("Invalid password. Please try again.");
                }
            } else {
                System.out.println("Account not found. Please register an account.");
            }
        } catch (IOException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void loggedInMenu() {
        int choice;

        do {
            printAccountOperationsMenu();
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addFunds();
                    break;
                case 2:
                    subtractFunds();
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    addCard();
                    break;
                case 5:
                    // Add other account operations
                    break;
                case 0:
                    return; // Exit to the previous menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private static void printAccountOperationsMenu() {
        System.out.println("===== Account Operations Menu =====");
        System.out.println("1. Add Funds");
        System.out.println("2. Subtract Funds");
        System.out.println("3. Check Balance");
        System.out.println("4. Add Card");
        System.out.println("5. Other Account Operations");
        System.out.println("0. Back to Previous Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addFunds() {
        System.out.print("Enter the card name: ");
        String cardName = scanner.next();
        System.out.print("Enter the amount to add: ");
        int amount = scanner.nextInt();

        try {
            AccountManager.addToBalance(ACCOUNT, cardName, amount);
            System.out.println("Funds added successfully. New balance for card " + cardName + ": " +
              AccountManager.getBalanceForCard(ACCOUNT, cardName));
        } catch (IOException e) {
            System.out.println("Error adding funds: " + e.getMessage());
        }
    }

    private static void subtractFunds() {
        System.out.print("Enter the card name: ");
        String cardName = scanner.next();
        System.out.print("Enter the amount to subtract: ");
        int amount = scanner.nextInt();

        try {
            int currentBalance = AccountManager.getBalanceForCard(ACCOUNT, cardName);

            if (currentBalance >= amount) {

                if (AccountManager.subtractFromBalance(ACCOUNT, cardName,
                  amount)) {
                    System.out.println("Funds subtracted successfully. New balance for card " +
                      cardName + ": " + AccountManager.getBalanceForCard(ACCOUNT, cardName));
                } else {
                    System.out.println("Error subtracting funds. Please try again later.");
                }
            } else {
                System.out.println("Insufficient funds. Unable to subtract.");
            }
        } catch (IOException e) {
            System.out.println("Error subtracting funds: " + e.getMessage());
        }
    }



    private static void checkBalance() {
        try {
            System.out.print("Enter the card name: ");
            String cardName = scanner.next();

            int balance = AccountManager.getBalanceForCard(ACCOUNT, cardName);
            System.out.println("Current balance for card " + cardName + ": " + balance);
        } catch (IOException e) {
            System.out.println("Error checking balance: " + e.getMessage());
            e.printStackTrace();  // Print the full stack trace for debugging
        }
    }


    private static void addCard() {
        System.out.print("Enter card name: ");
        String cardName = scanner.next();
        System.out.print("Enter currency (EUR, USDT, UAH): ");
        Currency currency = Currency.valueOf(scanner.next());

        try {
            if (AccountManager.createCard(ACCOUNT, cardName, currency)) {
                System.out.println("Card added successfully.");
            } else {
                System.out.println("Failed to add card. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error adding card: " + e.getMessage());
        }
    }
}
