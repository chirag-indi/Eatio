package com.hungrooz.www.hungrooz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<MenuOrderItem> menuList;

    public OrderMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order_menu, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.order_menu_recycler_view);

        menuList = new ArrayList<>();
        adapter = new MenuAdapter(getContext(), menuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        String menuType = getArguments().getString(Config.menuItemTag);
        String store = getArguments().getString(Config.menuStoreTag);
        int menuTypeNumber = 0;
        int storeIndex = 0;

        if (store.equals(Config.menuStoreNameJLTT)) {
            storeIndex = 0;
            if (menuType.equals(Config.menuBurger)) {
                menuTypeNumber = 0;
            } else if (menuType.equals(Config.menuSandwich)) {
                menuTypeNumber = 1;
            } else if (menuType.equals(Config.menuPasta)) {
                menuTypeNumber = 2;
            } else if (menuType.equals(Config.menuNoodles)) {
                menuTypeNumber = 3;
            }
        }

        if (store.equals(Config.menuStoreNameFoodAdda)) {
            storeIndex = 1;
            if (menuType.equals(Config.menuBurger)) {
                menuTypeNumber = 0;
            } else if (menuType.equals(Config.menuSandwich)) {
                menuTypeNumber = 1;
            } else if (menuType.equals(Config.menuMaggi)) {
                menuTypeNumber = 2;
            } else if (menuType.equals(Config.menuMacroni)) {
                menuTypeNumber = 3;
            }
        }
        prepareMenu(storeIndex, menuTypeNumber);

        return rootView;
    }

    private void prepareMenu(int storeIndex, int menuIndex) {

        String[] orderItem;
        String storeName;
        int[] price;
        int[] quantity;

        if (storeIndex == 0) {
            storeName = Config.menuStoreNameJLTT;
            JLTT store1 = new JLTT(menuIndex);
            orderItem = store1.getMenuItem();
            price = store1.getMenuPrice();
            quantity = store1.getMenuQuantity();
        } else {
            storeName = Config.menuStoreNameFoodAdda;
            FoodAdda store2 = new FoodAdda(menuIndex);
            orderItem = store2.getMenuItem();
            price = store2.getMenuPrice();
            quantity = store2.getMenuQuantity();
        }


        for (int i = 0; i < orderItem.length; i++) {
            MenuOrderItem menuOrderItem = new MenuOrderItem(orderItem[i], storeName, quantity[i], price[i]);
            menuList.add(menuOrderItem);
            adapter.notifyDataSetChanged();
        }
    }


    private class JLTT {
        private int menuItemNo;
        String[] burger = {"Veg Burger", "Tandoori Kabab Burger"};
        String[] sandwich = {"Bombay Grilled Sandwich", "Spinach Corn Sandwich"};
        String[] pasta = {"Pasta in Red Sauce", "Pasta in Red Sauce"};
        String[] noodles = {"Veg Hakka Noodles", "Schezwan Garlic Noodles"};
        String[][] menuItem = {burger, sandwich, pasta, noodles};

        int[] burgerPrice = {55, 65};
        int[] sandwichPrice = {50, 65};
        int[] pastaPrice = {80, 90};
        int[] noodlesPrice = {70, 80};
        int[][] menuItemPrice = {burgerPrice, sandwichPrice, pastaPrice, noodlesPrice};

        int[] burgerQuantity = {1, 1};
        int[] sandwichQuantity = {1, 1};
        int[] pastaQuantity = {1, 1};
        int[] noodlesQuantity = {1, 1};
        int[][] menuQuantity = {burgerQuantity, sandwichQuantity, pastaQuantity, noodlesQuantity};

        JLTT(int menuItemNo) {
            this.menuItemNo = menuItemNo;
        }

        String[] getMenuItem() {
            return menuItem[menuItemNo];
        }

        int[] getMenuPrice() {
            return menuItemPrice[menuItemNo];
        }

        int[] getMenuQuantity() {
            return menuQuantity[menuItemNo];
        }
    }

    private class FoodAdda {
        private int menuItemNo;
        String[] burger = {"Aloo Tikki Burger", "Chotu Maharaja Burger(with Paneer)",
                "Chotu Maharaja Burger(without Paneer)", "Additional cheese"};
        String[] sandwich = {"Veggie Sandwich", "Veggie Sandwich(Paneer)"};
        String[] maggi = {"Veg Maggi", "Fried Maggi", "Fried Egg Maggi", "Maggi Exotica Veg",
                "Maggi Exotica Egg"};
        String[] macaroni = {"Veg Macaroni"};
        String[][] menuItem = {burger, sandwich, maggi, macaroni};

        int[] burgerPrice = {60, 60, 50, 10};
        int[] sandwichPrice = {30, 40};
        int[] maggiPrice = {30, 40, 55, 60, 60};
        int[] macaroniPrice = {70};
        int[][] menuItemPrice = {burgerPrice, sandwichPrice, maggiPrice, macaroniPrice};

        int[] burgerQuantity = {1, 1, 1, 1};
        int[] sandwichQuantity = {1, 1};
        int[] maggiQuantity = {1, 1, 1, 1, 1};
        int[] macaroniQuantity = {1};
        int[][] menuQuantity = {burgerQuantity, sandwichQuantity, maggiQuantity, macaroniQuantity};

        FoodAdda(int menuItemNo) {
            this.menuItemNo = menuItemNo;
        }

        String[] getMenuItem() {
            return menuItem[menuItemNo];
        }

        int[] getMenuPrice() {
            return menuItemPrice[menuItemNo];
        }

        int[] getMenuQuantity() {
            return menuQuantity[menuItemNo];
        }
    }
}
