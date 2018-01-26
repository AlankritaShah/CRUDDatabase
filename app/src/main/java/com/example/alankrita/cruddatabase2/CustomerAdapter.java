package com.example.alankrita.cruddatabase2;

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

public class CustomerAdapter extends ArrayAdapter<Customer> {

    Context mCtx;
    int listLayoutRes;
    List<Customer> customerList;
    SQLiteDatabase mDatabasecustomer;

    public CustomerAdapter(Context mCtx, int listLayoutRes, List<Customer> customerList, SQLiteDatabase mDatabasecustomer) {
        super(mCtx, listLayoutRes, customerList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.customerList = customerList;
        this.mDatabasecustomer = mDatabasecustomer;
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

        Button buttonDelete = (Button) view.findViewById(R.id.buttonDeleteCustomer);
        Button buttonEdit =(Button) view.findViewById(R.id.buttonEditCustomer);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomer(customer);
                //  Log.i("customer",customer.getName());
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM customers WHERE phone = ?";
                        mDatabasecustomer.execSQL(sql, new String[]{customer.getPhone()});
                        reloadCustomersFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
    private void updateCustomer(final Customer customer) {
        // SQLiteDatabase db = this.getWritableDatabase();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_customer, null);
        builder.setView(view);

        final EditText editTextName = (EditText) view.findViewById(R.id.editTextName);
        final EditText editTextPhone = (EditText) view.findViewById(R.id.editTextPhone);

        editTextName.setText(customer.getName());
        editTextPhone.setText(String.valueOf(customer.getPhone()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    editTextPhone.setError("Phone can't be blank");
                    editTextPhone.requestFocus();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put("name",name);
                cv.put("phone",phone);
                mDatabasecustomer.update("customers", cv, "phone=" + customer.getPhone(), null);

                Toast.makeText(mCtx, "Customer Updated", Toast.LENGTH_SHORT).show();
                reloadCustomersFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadCustomersFromDatabase() {
        Cursor cursorThings = mDatabasecustomer.rawQuery("SELECT * FROM customers", null);
        if (cursorThings.moveToFirst()) {
            customerList.clear();
            do {
                customerList.add(new Customer(
                        cursorThings.getString(0),
                        cursorThings.getString(1)
                ));
                //  Log.i("cursorname", cursorThings.getString(1));
            } while (cursorThings.moveToNext());

        }
        //  Log.i("cursorThings", cursorThings.getString(1));
        cursorThings.close();
        notifyDataSetChanged();
    }
}