package com.example.alankrita.cruddatabase2;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ALANKRITA on 19-01-2018.
 */
public class SearchActivity extends AppCompatActivity {
    List<Thing> billList;
    SQLiteDatabase mDatabasebill, mDatabase;
    ListView listViewThings;
    String barcodenumber;
    Button billbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //getting the barcode from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            barcodenumber = null;
        } else {
            barcodenumber = extras.getString("barcodenumber");
        }
        billbutton = (Button) findViewById((R.id.buttonbill));
        listViewThings = (ListView) findViewById(R.id.listViewThings);
        billList = new ArrayList<>();

        //opening the database
        mDatabasebill = openOrCreateDatabase(MainActivity.DATABASE_NAME_BILL, MODE_PRIVATE, null);
        //   SQLiteDatabase db = this.getWritableDatabase();
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        createBillTable();
        //this method will display the things in the list
        searchThingsFromDatabase();

        billbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, BillActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    //creates bill table
    private void createBillTable() {
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


    private void searchThingsFromDatabase() {

        Cursor cursorThings = mDatabase.rawQuery("SELECT * FROM things where barcodenumber = ?", new String[]{barcodenumber});
        Log.i("cursor", String.valueOf(cursorThings.getCount()));

        //if the item is not available in the local database, search in firebase
        if (cursorThings.getCount() == 0) {
            //  Firebase myFirebaseRef = new Firebase("https://myfirebaseurl.firebaseio.com/android/saving-data/fireblog");
            //  Query queryRef = myFirebaseRef.orderByChild("barcodenumber").equalTo(barcodenumber);

//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            final DatabaseReference databaseItems;

            databaseItems = FirebaseDatabase.getInstance().getReference("Products");
//            Query queryRef = databaseItems.orderByChild("barcodenumber").equalTo(barcodenumber);
//            Log.i("queryRef", queryRef.toString());
            databaseItems.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("ds", dataSnapshot.toString());
                    if (dataSnapshot != null || dataSnapshot.getChildrenCount() > 0) {
                        collectProducts((Map<String, Object>) dataSnapshot.getValue());
//                        Query queryRef = databaseItems.orderByChild("barcodenumber").equalTo(barcodenumber);
//                        Log.i("query", queryRef.toString());
                        // List prodlist = new ArrayList<>();
//                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                            Log.i("nds", noteDataSnapshot.toString());
//                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Toast.makeText(getApplicationContext(), "Failed to read value." + databaseError.toException(), Toast.LENGTH_LONG).show();

                }
            });

        }

            if (cursorThings.moveToFirst()) {
                //looping through all the records
                do {
                    //pushing the values to the bill table
                    Log.i("name", cursorThings.getString(1));
                    mDatabasebill.execSQL("INSERT INTO Bill" + MainActivity.i + "\n" +
                            "(name, unit, stock, price, barcodenumber)\n" +
                            "VALUES \n" +
                            "(?, ?, ?, ?, ?);", new String[]{
                            cursorThings.getString(1),
                            cursorThings.getString(2),
                            cursorThings.getString(3),
                            cursorThings.getString(4),
                            cursorThings.getString(5)});

                    Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                    billList.add(new Thing(
                            cursorThings.getInt(0),
                            cursorThings.getString(1),
                            cursorThings.getString(2),
                            cursorThings.getDouble(3),
                            cursorThings.getDouble(4),
                            cursorThings.getString(5)
                    ));
                } while (cursorThings.moveToNext());
            }
            //closing the cursor
            cursorThings.close();
            listViewThings.setAdapter(new SearchAdapter(this, R.layout.list_layout_search, billList, mDatabasebill));
        }

    //for retrieving data from firebase
    private void collectProducts(Map<String, Object> prod) {
        if (prod != null && !prod.isEmpty()) {

            for (Map.Entry<String, Object> entry : prod.entrySet()) {

                Map singleUser = (Map) entry.getValue();

                String namefb = (String) singleUser.get("name");
                String pricefb = singleUser.get("price").toString();
                String stockfb = singleUser.get("stock").toString();
                String barcodenumberfb = singleUser.get("barcodenumber").toString();
                String utfb  = singleUser.get("ut").toString();
//                Log.i("name", namefb);
//                Log.i("barcodenumber", barcodenumberfb);
                if(barcodenumberfb.equals(barcodenumber)){
                    Log.i("namefb", namefb);
                    // insert in the bill table from the firebase
                    mDatabasebill.execSQL("INSERT INTO Bill" + MainActivity.i + "\n" +
                            "(name, unit, stock, price, barcodenumber)\n" +
                            "VALUES \n" +
                            "(?, ?, ?, ?, ?);", new String[]{
                            namefb, utfb, stockfb, pricefb, barcodenumberfb});

                    //insert in the table of kirana store
                    mDatabase.execSQL("INSERT INTO things \n" +
                            "(name, unit, stock, price, barcodenumber)\n" +
                            "VALUES \n" +
                            "(?, ?, ?, ?, ?);", new String[]{namefb, utfb, stockfb, pricefb, barcodenumberfb});
                    //searchThingsFromDatabase();
                }
                // contacts.add(new Appointment(date, time, doctor, patient));

// Toast.makeText(getApplicationContext(), "Date: " + date + ", Time:" + time + ", Doctor:" + doctor + ", Patient:" + patient, Toast.LENGTH_LONG).show();

                //  adapter.notifyDataSetChanged();
            }
        }
    }
}

