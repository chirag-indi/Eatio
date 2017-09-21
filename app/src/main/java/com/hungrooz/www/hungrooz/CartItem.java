package com.hungrooz.www.hungrooz;

/**
 * Created by Ayush894 on 25-04-2017.
 */

public class CartItem {

    private String orderItem;
    private String store;
    private int quantity;
    private int price;

    public CartItem() {
    }

    public CartItem(String orderItem, String store, int quantity, int price) {
        this.orderItem = orderItem;
        this.store = store;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
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
}
