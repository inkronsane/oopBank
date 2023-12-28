package com.inkronsane.bank.ui;


import com.inkronsane.bank.bl.*;
import com.inkronsane.bank.data.Currency;
import java.io.*;
import java.util.*;

public class UI {

    private static final Scanner scanner = new Scanner(System.in);
    private static int creditLimit = 0;
    private static BankSystem.Account ACCOUNT;

    public static void menu() throws IOException {
        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0 -> System.out.println("Кудааааааааа");
                case 1 -> registration();
                case 2 -> authorization();
                default -> System.out.println("Вибери нормально");
            }
        } while (choice != 0);
    }

    static void displayMenu() {
        System.out.println("=====My menu=====");
        System.out.println("1) Реєстрація");
        System.out.println("2) Авторизація");
        System.out.println("3) Випилитись");
    }

    static void registration() throws IOException {
        System.out.println("======My registration======");
        System.out.println("Ваше ім'я");
        String firstName = scanner.nextLine();
        System.out.println("Ваша фамілія");
        String lastName = scanner.nextLine();
        System.out.println("Айді вашого паспорту");
        int passportId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Придумайте пароль");
        String password = scanner.nextLine();
        System.out.println("Повторіть ваш пароль");
        String password2 = scanner.nextLine();
        if (!password2.equals(password)) {
            System.out.println("Вам заборонено користуватись цим банком, щасливого дня!");
            System.exit(0);
        }
        if (BankSystem.accountExists(passportId)) {
            System.out.println("Такий айді паспорту вже зареєстровано!");
        } else {
            if (BankSystem.createAccount(firstName, lastName, passportId, password)) {
                System.out.println("Successful!");
            } else {
                System.out.println("Тут даже я хз");
            }
        }
    }

    static void authorization() throws IOException {
        System.out.println("Айді вашого паспорту:");
        int passportId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ваш пароль: ");
        String password = scanner.nextLine();
        if (BankSystem.authenticatePassword(passportId, password)) {
            ACCOUNT = BankSystem.authenticate(passportId);
            System.out.println("Зірки зівпали, ви увійшли!");
            accountMenu();
        } else {
            System.out.println("пароль невірний");
        }
    }

    static void accountMenu() throws IOException {
        int vubor;
        do {
            printAccountMenu();
            vubor = scanner.nextInt();
            scanner.nextLine();
            switch (vubor) {
                case 0 -> {
                    ACCOUNT = null;
                    System.out.println("Tot ziens!");
                    menu();
                }
                case 1 -> credit();
                case 2 -> seeInformation();
                case 3 -> PurchaseManager.processPurchase(ACCOUNT);
                case 4 -> createCard();
                default -> System.out.println("Error");
            }

        } while (vubor != 0);
    }

    public static void seeInformation() throws IOException {
        System.out.println("1. Display Account Information");
        System.out.println(
          "2. Display Card Information");//заставляю іі работат на благо людства(моє благо)
        System.out.println("3. Display Logs");
        System.out.println("4) Ваші картки");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> BankSystem.displayAccountInformation(ACCOUNT);
            case 2 -> {
                System.out.println("Enter Card Name: ");
                String cardName = scanner.nextLine();
                BankSystem.displayCardInformation(ACCOUNT, cardName);
            }
            case 3 -> BankSystem.displayLogs(ACCOUNT);
            case 4 -> System.out.println(BankSystem.listFilesInDirectory(ACCOUNT));
            default -> System.out.println("Invalid choice.");
        }
    }

    public static void createCard() throws IOException {
        System.out.println("Your card name: ");
        String name = scanner.nextLine();// ya ustav
        System.out.println("Your card currency: ");
        Currency currency = getCurrencyChoice();
        if (BankSystem.createCard(ACCOUNT, name, currency)) {
            System.out.println("Ваша картка схвалена!");
            System.out.println("Ім'я картки: " + name);
            System.out.println("Валюта картки: " + currency);
        } else {
            System.out.println("Картка з таким ім'ям вже існує!");
        }
    }

    private static Currency getCurrencyChoice() {
        while (true) {
            System.out.println("USDT, EUR, UAH(ignoreCase)");
            String choice = scanner.nextLine().toUpperCase();
            switch (choice) {
                case "USDT":
                    return Currency.USDT;
                case "EUR":
                    return Currency.EUR;
                case "UAH":
                    return Currency.UAH;
                default:
                    System.out.println("Invalid choice. Please choose from USDT, EUR, UAH.");
            }
        }
    }

    public static void credit() throws IOException {
        System.out.println("=====Debt=====");
        System.out.println("Уведіть ім'я картки для якої буде зараховано кредит: ");
        String cardName = scanner.nextLine();
        System.out.println("Уведіть сумму: ");
        int sum = scanner.nextInt();
        scanner.nextLine();
        if (creditLimit < 3) {
            if (BankSystem.addToBalance(ACCOUNT, cardName, sum)) {
                System.out.println("Ваш кредит схвалено!");
                creditLimit++;
            }
        } else {
            System.out.println("Hажаль ваш кредитний ліміт не дозволяє вам взяти більше кредитів!");
        }
    }

    static void printAccountMenu() {
        System.out.println("=====Меню аккаунту=====");
        System.out.println("0) Ініціалізувати вихід з аккаунту");
        System.out.println("1) Взяти кредит на сайті(невиплатишкредит.юа)");
        System.out.println("2) Переглянути інформацію стосовно аккаунту");
        System.out.println(
          "3) Відкрити онлайн магазин(все що ви купуєте буде доставлено поштою ВСУ)");
        System.out.println("4) Створити картку");
    }
}
