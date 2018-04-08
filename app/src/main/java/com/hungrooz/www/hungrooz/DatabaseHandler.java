package com.hungrooz.www.hungrooz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ayush894 on 25-04-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CartManager";
    private static final String TABLE_CART = "orderCart";

    private static final String KEY_ORDER_ITEM = "order_item";
    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_ITEM_COUNT = "item_count";
    private static final String KEY_PRICE = "order_price";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ORDER_ITEM + " VARCHAR," + KEY_STORE_NAME + " VARCHAR,"
                + KEY_ITEM_COUNT + " VARCHAR," + KEY_PRICE + " VARCHAR" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        // Create tables again
        onCreate(db);
    }

    void addOrderToCartDB(String order, String store, String quantity, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + KEY_ORDER_ITEM + "=\'" + order +
                "\' AND " + KEY_STORE_NAME + "=\'" + store + "\' AND " + KEY_PRICE + "=\'" + price + "\'", null);

        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            updateCartItem(order, store, quantity, price);
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_ORDER_ITEM, order);
            values.put(KEY_STORE_NAME, store);
            values.put(KEY_ITEM_COUNT, quantity);
            values.put(KEY_PRICE, price);

            // Inserting Row
            db.insert(TABLE_CART, null, values);
            db.close(); // Closing database connection
        }
    }


    int getCartItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    String[][] readFromCartDB() {
        String selectQuery = "SELECT * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;
        int n = cursor.getCount();
        String[][] orderList = new String[n][4];
        while (cursor.moveToNext()) {
            orderList[i][0] = cursor.getString(0);
            orderList[i][1] = cursor.getString(1);
            orderList[i][2] = cursor.getString(2);
            orderList[i][3] = cursor.getString(3);
            i++;
        }
        cursor.close();
        return orderList;
    }

    public void updateCartItem(String order, String store, String quantity, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_CART + " SET " + KEY_ITEM_COUNT + "=\'" + quantity +
                "\' WHERE " + KEY_ORDER_ITEM + "=\'" + order + "\' AND " + KEY_STORE_NAME + "=\'" +
                store + "\' AND " + KEY_PRICE + "=\'" + price + "\'";

        db.execSQL(updateQuery);
        db.close();
    }

    public void removeFromCart(String order, String store, String quantity, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_CART + " WHERE " + KEY_ORDER_ITEM + "=\'" + order +
                "\' AND " + KEY_STORE_NAME + "=\'" + store + "\' AND " + KEY_ITEM_COUNT + "=\'" +
                quantity + "\' AND " + KEY_PRICE + "=\'" + price + "\'";

        db.execSQL(deleteQuery);
        db.close();
    }
}
