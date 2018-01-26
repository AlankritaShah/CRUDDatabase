package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 24-01-2018.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewActivity extends AppCompatActivity {

    List<Customer> customerList;
    SQLiteDatabase mDatabasecustomer;
    ListView listViewCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listViewCustomers = (ListView) findViewById(R.id.listViewCustomers);
        customerList = new ArrayList<>();

        //opening the database
        mDatabasecustomer = openOrCreateDatabase(MainActivity.DATABASE_CUSTOMER, MODE_PRIVATE, null);
        //   SQLiteDatabase db = this.getWritableDatabase();

        //this method will display the customers in the list
        showCustomersFromDatabase();
    }

    private void showCustomersFromDatabase() {

        //fetching all the things
        Cursor cursorThings = mDatabasecustomer.rawQuery("SELECT * FROM customers", null);

        //if the cursor has some data
        if (cursorThings.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the thing list
                customerList.add(new Customer(
                        cursorThings.getString(0),
                        cursorThings.getString(1)
                ));
            } while (cursorThings.moveToNext());
        }
        //closing the cursor
        cursorThings.close();

        listViewCustomers.setAdapter(new CustomerAdapter(this, R.layout.list_layout_customer, customerList, mDatabasecustomer));
    }
}