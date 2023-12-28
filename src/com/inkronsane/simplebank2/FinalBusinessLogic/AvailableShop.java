package com.inkronsane.simplebank2.FinalBusinessLogic;


import com.inkronsane.bank.data.*;
import java.util.*;

public class AvailableShop {

    List<GoodsSell> goodsSellList = new ArrayList<>();

    public AvailableShop() {
        generateGoodsPrice();
    }

    public AvailableShop(List<GoodsSell> goodsSellList) {
        this.goodsSellList = goodsSellList;
    }

    public int getGoodsCostByIndex(int index) {
        if (index >= 0 && index < goodsSellList.size()) {
            return goodsSellList.get(index).price;
        }
        return 0;
    }

    public void generateGoodsPrice() {
        Random random = new Random();

        int id = 0;
        for (Goods goods : Goods.values()) {
            int price = random.nextInt(200);
            Currency currency = getRandomCurrency();
            goodsSellList.add(new GoodsSell(goods.getName(), price, id, currency));
            id++;
        }
    }

    private Currency getRandomCurrency() {
        Random random = new Random();
        Currency[] currencies = Currency.values();
        return currencies[random.nextInt(currencies.length)];
    }//vot

    public List<GoodsSell> getGoodsSellList() {
        return goodsSellList;
    }

    public class GoodsSell {

        String name;
        int price;
        int id;
        Currency currency;

        public GoodsSell(String name, int price, int id, Currency currency) {
            this.name = name;
            this.price = price;
            this.id = id;
            this.currency = currency;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public Currency getCurrency() {
            return currency;
        }
    }
}
