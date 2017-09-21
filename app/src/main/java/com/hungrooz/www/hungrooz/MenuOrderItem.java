package com.hungrooz.www.hungrooz;

/**
 * Created by Ayush894 on 25-04-2017.
 */

public class MenuOrderItem {

    private String menuItem;
    private String storeName;
    private int quantity;
    private int price;

    public MenuOrderItem() {
    }

    public MenuOrderItem(String menuItem, String storeName, int quantity, int price) {
        this.menuItem = menuItem;
        this.storeName = storeName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
