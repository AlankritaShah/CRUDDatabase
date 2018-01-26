package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 24-01-2018.
 */
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomerAddActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewViewCustomers;
    EditText editTextName, editPhone; //two fields for customer

    SQLiteDatabase mDatabasecustomer, mDatabaseCT;

  //  DatabaseReference databaseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customeradd);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        databaseItems = FirebaseDatabase.getInstance().getReference("Customers");  //Firebase table

        //creating a database
        mDatabasecustomer = openOrCreateDatabase(MainActivity.DATABASE_CUSTOMER, MODE_PRIVATE, null);
        mDatabaseCT = openOrCreateDatabase(MainActivity.DATABASE_CUSTOMER_THING, MODE_PRIVATE, null);

        createCustomerTable();

        textViewViewCustomers = (TextView) findViewById(R.id.textViewViewCustomers);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editPhone = (EditText) findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonAddCustomer).setOnClickListener(this);
        textViewViewCustomers.setOnClickListener(this);
    }

    private void createCustomerTable() {
        //query to create the table in local database
        mDatabasecustomer.execSQL(
                "CREATE TABLE IF NOT EXISTS customers (\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    phone INTEGER PRIMARY KEY NOT NULL\n" +
                        ");"
        );
    }

    private boolean inputsAreCorrect(String name, String phone) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (phone.isEmpty() || Integer.parseInt(phone) <= 0) {
            editPhone.setError("Please enter phone number");
            editPhone.requestFocus();
            return false;
        }

        return true;
    }

    //In this method we will do the create operation
    private void addCustomer() {

        String name = editTextName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        //validating the inptus
        if (inputsAreCorrect(name, phone)) {
            //Unique id for the rows in firebase table
//            String idd = databaseItems.push().getKey();
//            Thing th = new Thing(1, name, ut, 0 , Double.parseDouble(price), barcodenumber);
//            databaseItems.child(idd).setValue(th);

            //query to insert values in the local database
            mDatabasecustomer.execSQL("INSERT INTO customers \n" +
                    "(name, phone)\n" +
                    "VALUES \n" +
                    "(?, ?);", new String[]{name, phone});

            Toast.makeText(this, "Customer Added Successfully", Toast.LENGTH_SHORT).show();

            mDatabaseCT.execSQL("INSERT INTO customerthing \n" + "(customer)" + "VALUES \n" + "(?);", new String[] {phone});
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddCustomer:

                addCustomer();

                break;
            case R.id.textViewViewCustomers:

                startActivity(new Intent(this, CustomerViewActivity.class));
        }
    }
}