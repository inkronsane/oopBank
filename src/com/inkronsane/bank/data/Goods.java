package com.inkronsane.bank.data;


import java.util.*;

public enum Goods {
    ABRIKOSA("Abrikosa", Currency.USDT, 10),
    KROPIVA("Kropiva", Currency.EUR, 1000),
    VEPERKA("Veperka", Currency.UAH, 5),
    DOTA("Dota dva", Currency.UAH, 1),
    PIVO("Lvivske pivo 5%", Currency.USDT, 1000),
    BANAN("Банан Ужгородський", Currency.UAH, 1000000);
    String name;
    Currency currency;
    int price;

    Goods(String name, Currency currency, int price) {
        this.name = name;
        this.currency = currency;
        this.price = price;
    }

    public static List<Goods> getAvailableGoods() {
        List<Goods> availableGoods = new ArrayList<>();
        availableGoods.add(ABRIKOSA);
        availableGoods.add(KROPIVA);
        availableGoods.add(VEPERKA);
        availableGoods.add(DOTA);
        availableGoods.add(PIVO);
        availableGoods.add(BANAN);
        return availableGoods;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency.name();
    }

    public int getPrice() {
        return price;
    }
}
