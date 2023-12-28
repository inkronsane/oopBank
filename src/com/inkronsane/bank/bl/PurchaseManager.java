package com.inkronsane.bank.bl;


import com.inkronsane.bank.data.*;
import java.util.*;

public class PurchaseManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Goods> availableGoods = Goods.getAvailableGoods();

    public static void showAvailableGoods() {
        System.out.println("Доступні товари:");
        for (Goods good : availableGoods) {
            System.out.println(
              good.getName() + " - Ціна: " + good.getPrice() + " " + good.getCurrency());
        }
    }

    public static void processPurchase(BankSystem.Account account) {
        showAvailableGoods();
        System.out.println("Введіть ім'я товару, який ви хочете купити:");
        String goodsName = scanner.nextLine();
        Goods selectedGoods = findGoodsByName(goodsName);
        if (selectedGoods != null) {
            System.out.println("Введіть кількість товару, яку ви хочете купити:");
            int quantity = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введіть ім'я картки, якою ви хочете розрахуватися:");
            String cardName = scanner.nextLine();
            try {
                String goodsCurrency = selectedGoods.getCurrency();
                int totalCost = selectedGoods.getPrice() * quantity;
                if (Objects.equals(goodsCurrency, FileEditor.findInFile(
                  account.getCardsDirectory() + "/" + cardName + ".txt",
                  BankSystem.CARD_CURRENCY))) {
                    if (accountHasSufficientFunds(account, totalCost, cardName)) {
                        System.out.println("Покупка успішна!");
                        deductAmountFromCard(account, totalCost, goodsCurrency, cardName);
                        BankSystem.logWrite(account, cardName, selectedGoods, quantity);
                    } else {
                        System.out.println(
                          "Недостатньо коштів на обраній картці. Покупка не вдалася.");
                    }
                } else {
                    System.out.println("Несумісність валют!");
                }

            } catch (Exception e) {
                System.out.println("Сталася помилка при обробці покупки: " + e.getMessage());
            }
        } else {
            System.out.println("Товар з ім'ям " + goodsName + " не знайдений.");
        }
    }

    private static Goods findGoodsByName(String name) {
        for (Goods good : availableGoods) {
            if (good.getName().equalsIgnoreCase(name)) {
                return good;
            }
        }
        return null;
    }

    private static boolean accountHasSufficientFunds(BankSystem.Account account, int totalCost,
      String cardName) throws Exception {
        int cardBalance = BankSystem.getBalanceForCard(account, cardName);
        return cardBalance >= totalCost;
    }

    private static void deductAmountFromCard(BankSystem.Account account, int totalCost,
      String goodsCurrency, String cardName) throws Exception {
        if (BankSystem.subtractToBalance(account, cardName, totalCost)) {
            System.out.println("Списано " + totalCost + " " + goodsCurrency + " з рахунку.");
        } else {
            throw new Exception("Не вдалося зняти кошти з рахунку.");
        }
    }
}
