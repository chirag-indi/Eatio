package com.hungrooz.www.hungrooz;

/**
 * Created by Ayush894 on 09-04-2017.
 */

public class Config {

    public static final String URL_ADD_CUSTOMER = "http://trykaro.16mb.com/registerCustomer.php";
    public static final String URL_UPDATE_CUSTOMER = "http://trykaro.16mb.com/updateDetails.php";
    public static final String URL_GET_CUSTOMER = "http://trykaro.16mb.com/loginDetails.php";
    public static final String URL_ADD_TO_CART = "http://trykaro.16mb.com/addToCart.php";
    public static final String URL_GET_CART_DETAILS = "http://trykaro.16mb.com/getFromCart.php";
    public static final String URL_REMOVE_FROM_CART = "http://trykaro.16mb.com/removeFromCart.php";
    public static final String URL_CUSTOMER_ORDER_COUNT = "http://trykaro.16mb.com/getCustomerOrderCount.php";
    public static final String URL_ORDER_CONFIRM = "http://trykaro.16mb.com/completeOrderDone.php";
    public static final String URL_GUEST_ORDER_CONFIRM_DETAILS = "http://trykaro.16mb.com/completeOrderDoneDetails.php";
    public static final String URL_REGISTERED_ORDER_CONFIRM_DETAILS = "http://trykaro.16mb.com/cartToDetail.php";
    public static final String URL_YOUR_ORDERS = "http://trykaro.16mb.com/getYourOrders.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_CUSTOMER_NAME = "name";
    public static final String KEY_CUSTOMER_PHONE = "phone";
    public static final String KEY_CUSTOMER_PASSWORD = "password";
    public static final String KEY_CUSTOMER_EMAIL = "email";
    public static final String KEY_CUSTOMER_DOB = "dob";
    public static final String KEY_CUSTOMER_ADDRESS = "address";
    public static final String KEY_CUSTOMER_GENDER = "gender";

    //JSON Tags
    public static final String TAG_JSON_ARRAY = "result";

    //Shared Preference Login/Profile Tags
    public static final String sharedName = "nameKey";
    public static final String sharedPhone = "phoneKey";
    public static final String sharedPassword = "passwordKey";
    public static final String sharedEmail = "emailKey";
    public static final String sharedGender = "genderKey";
    public static final String sharedDOB = "dobKey";
    public static final String sharedAddress = "addressKey";
    public static final String sharedLoginPREFERENCES = "LogPrefs";

    // Order Menu Tags
    public static final String menuItemTag = "MenuItemType";
    public static final String menuStoreTag = "MenuStoreType";
    public static final String menuBurger = "Burger";
    public static final String menuSandwich = "Sandwich";
    public static final String menuMaggi = "Maggi";
    public static final String menuPasta = "Pasta";
    public static final String menuMacroni = "Macaroni";
    public static final String menuNoodles = "Noodles";
    public static final String menuStoreNameJLTT = "Dollops";
    public static final String menuStoreNameFoodAdda = "Saiba";

    // Cart Keys for PHP
    public static final String KEY_CART_PHONE = "phone";
    public static final String KEY_CART_ORDER_ITEM = "order_item";
    public static final String KEY_CART_STORE = "store_name";
    public static final String KEY_CART_QUANTITY = "item_count";
    public static final String KEY_CART_PRICE = "price";
    public static final String KEY_CART_ITEM_COUNT = "cart_count";

    // Place Order Tags
    public static final String tempSharedName = "tempName";
    public static final String tempSharedPhone = "tempPhone";
    public static final String tempSharedEmail = "tempEmail";
    public static final String tempSharedGender = "tempGender";
    public static final String tempSharedDob = "tempDob";
    public static final String tempSharedAddress = "tempAddress";
    public static final String tempCustomerPreferences = "tempPrefs";
    public static final String finalOrderIdTag = "orderID";
    public static final String tempCountPreviousOrder = "tempPreviousOrders";
    public static final String tempTotalPrice = "tempTotalBill";

    // Final Submission on Order Confirm Keys
    public static final String KEY_CONFIRM_ORDER_PHONE = "phone";
    public static final String KEY_CONFIRM_ORDER_ORDER_ID = "order_id";
    public static final String KEY_CONFIRM_ORDER_TOTAL_PRICE = "total_price";
    public static final String KEY_CONFIRM_ORDER_TIME = "time";
    public static final String KEY_CONFIRM_ORDER_ADDRESS = "address";
    public static final String KEY_CONFIRM_ORDER_PAYMENT_MODE = "payment_mode";
    public static final String KEY_CONFIRM_ORDER_PROMO_CODE = "promo_code";
    public static final String KEY_CONFIRM_ORDER_STORE_NAME = "store_name";
    public static final String KEY_CONFIRM_ORDER_ORDER_ITEM = "order_item";
    public static final String KEY_CONFIRM_ORDER_ITEM_COUNT = "item_count";
    public static final String KEY_CONFIRM_ORDER_PRICE = "price";

}
