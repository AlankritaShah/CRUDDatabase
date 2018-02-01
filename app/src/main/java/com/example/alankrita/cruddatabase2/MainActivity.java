package com.example.alankrita.cruddatabase2;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "myitemlist7"; //items available with the kirana store
    public static final String DATABASE_NAME_BILL = "mybilllist7"; //Billing database
    public static final String DATABASE_CUSTOMER = "mycustomerlist"; //database to store the list of customers
    public static final String DATABASE_CUSTOMER_THING = "mycustomerthinglist"; //combined table of customer and items

    static int i=0; //Variable for the billing table - Bill0, Bill1, Bill2....
    static int itemcount=0;
    TextView textViewViewThings;
    EditText editTextName, editTextStock, editTextPrice, editTextbarcodenumber;
    Spinner spinnerUnit;
    Button scanbarcode;
    Button customerbutton;

    SQLiteDatabase mDatabase, mDatabaseCT;

    DatabaseReference databaseItems;
  //  private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseItems = FirebaseDatabase.getInstance().getReference("Products");  //Firebase table

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        mDatabaseCT = openOrCreateDatabase(DATABASE_CUSTOMER_THING, MODE_PRIVATE, null);

        createCustomerThingTable();
        createThingTable();

        customerbutton = (Button) findViewById(R.id.customerbutton);
        scanbarcode = (Button) findViewById(R.id.scanbarcode);
        editTextbarcodenumber = (EditText) findViewById(R.id.editTextbarcodenumber);
        textViewViewThings = (TextView) findViewById(R.id.textViewViewThings);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextStock = (EditText) findViewById(R.id.editTextStock);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        spinnerUnit = (Spinner) findViewById(R.id.spinnerUnit);

        findViewById(R.id.buttonAddThing).setOnClickListener(this);
        textViewViewThings.setOnClickListener(this);

      //  qrScan = new IntentIntegrator(this);
        scanbarcode.setOnClickListener(this);
        customerbutton.setOnClickListener(this);
    }

    //Getting the scan results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                Log.i("result",result.toString());
//                String barcodenumber = result.getContents();
//
//                Intent in = new Intent(this, SearchActivity.class);
//                in.putExtra("barcodenumber",barcodenumber);
//                startActivity(in);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    private void createCustomerThingTable() {
        mDatabaseCT.execSQL(
                "CREATE TABLE IF NOT EXISTS customerthing (\n" +
                        "    customer varchar(200) NOT NULL\n" +
                        ");"
        );
    }

    private void createThingTable() {
        //query to create the table in local database
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS things (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    unit varchar(200) NOT NULL,\n" +
                        "    stock double NOT NULL,\n" +
                        "    price double NOT NULL,\n" +
                        "    barcodenumber varchar(200) NOT NULL\n" +
                        ");"
        );
    }

    private boolean inputsAreCorrect(String name, String stock, String price) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (stock.isEmpty() || Integer.parseInt(stock) <= 0) {
            editTextStock.setError("Please enter stock");
            editTextStock.requestFocus();
            return false;
        }

        if (price.isEmpty() || Integer.parseInt(stock) <= 0) {
            editTextPrice.setError("Please enter price");
            editTextPrice.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addThing() {

        String name = editTextName.getText().toString().trim();
        String stock = editTextStock.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String ut = spinnerUnit.getSelectedItem().toString();
        String barcodenumber = editTextbarcodenumber.getText().toString().trim();


        //validating the inptus
        if (inputsAreCorrect(name, stock, price)) {
            //Unique id for the rows in firebase table
            String idd = databaseItems.push().getKey();
            Thing th = new Thing(1, name, ut, 0 , Double.parseDouble(price), barcodenumber);
            databaseItems.child(idd).setValue(th);

            //query to insert values in the local database
            mDatabase.execSQL("INSERT INTO things \n" +
                    "(name, unit, stock, price, barcodenumber)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?);", new String[]{name, ut, stock, price, barcodenumber});

            Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

            mDatabaseCT.execSQL("ALTER TABLE customerthing \n" +
                    "ADD B" + barcodenumber + " INTEGER");
            itemcount++;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddThing:

                addThing();

                break;
            case R.id.textViewViewThings:

                startActivity(new Intent(this, ThingActivity.class));

                break;

            case R.id.scanbarcode:
                startActivity(new Intent(this, ContinousCaptureActivity.class));

//                qrScan.setOrientationLocked(false);
//
//                qrScan.initiateScan();
                break;

            case R.id.customerbutton:

                startActivity(new Intent(this, CustomerAddActivity.class));
        }
    }
}
