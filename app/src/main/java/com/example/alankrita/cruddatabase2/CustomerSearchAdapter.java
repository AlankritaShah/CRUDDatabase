package com.example.alankrita.cruddatabase2;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALANKRITA on 24-01-2018.
 */
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by ALANKRITA on 24-01-2018.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomerSearchAdapter extends ArrayAdapter<Customer> {

    Context mCtx;
    int listLayoutRes;
    List<Customer> customerList;
    SQLiteDatabase mDatabasecustomer, mDatabasebill;
    SQLiteDatabase mDatabaseCT;
    List<Thing> billList;

    public CustomerSearchAdapter(Context mCtx, int listLayoutRes, List<Customer> customerList, SQLiteDatabase mDatabasecustomer, SQLiteDatabase mDatabaseCT, List<Thing> billList, SQLiteDatabase mDatabasebill) {
        super(mCtx, listLayoutRes, customerList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.customerList = customerList;
        this.mDatabasecustomer = mDatabasecustomer;
        this.mDatabaseCT = mDatabaseCT;
        this.billList=billList;
        this.mDatabasebill = mDatabasebill;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Customer customer = customerList.get(position);

        TextView textViewName =(TextView) view.findViewById(R.id.textViewName);
        TextView textViewPhone =(TextView) view.findViewById(R.id.textViewPhone);

        textViewName.setText(customer.getName());
        textViewPhone.setText(customer.getPhone());

        Button buttonselectcustomer = (Button) view.findViewById(R.id.buttonSelectCustomer);

        //adding a clicklistener to button
        buttonselectcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("customer", customer.getName());
                Log.i("Bill",billList.toString());

                Cursor cursorThings = mDatabasebill.rawQuery("SELECT * FROM Bill" + MainActivity.i, null);
                Log.i("ct", String.valueOf(cursorThings.moveToFirst()));
                //if the cursor has some data
                if (cursorThings.moveToFirst()) {
                    //looping through all the records

                    do {
                        Log.i("billitemname", cursorThings.getString(1));
                        String bcode = "B"+ cursorThings.getString(5);
                        ContentValues cv = new ContentValues();
                        cv.put(bcode,1);
                        mDatabaseCT.update("customerthing", cv, "customer=" + customer.getPhone(), null);

                    } while (cursorThings.moveToNext());
                }
                //closing the cursor
                cursorThings.close();

                Cursor cursorThings2 = mDatabaseCT.rawQuery("SELECT * FROM customerthing", null);
                Log.i("ct", String.valueOf(cursorThings2.moveToFirst()));
                //if the cursor has some data
                if (cursorThings2.moveToFirst()) {
                    //looping through all the records

                    do {
                        Log.i("ct20", cursorThings2.getString(0));
                        Log.i("ct21", cursorThings2.getString(1));
                        Log.i("ct22", cursorThings2.getString(2));
                    } while (cursorThings2.moveToNext());
                }
                //closing the cursor
                cursorThings2.close();

                MainActivity.i++;
                createNewBillTable();
            }
        });
        return view;
    }

    public void createNewBillTable() {
     //creates new bill table
        mDatabasebill.execSQL(
                "CREATE TABLE IF NOT EXISTS Bill" + MainActivity.i + "(\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    unit varchar(200) NOT NULL,\n" +
                        "    stock double NOT NULL,\n" +
                        "    price double NOT NULL,\n" +
                        "    barcodenumber varchar(200) NOT NULL\n" +
                        ");"
        );
    }
}