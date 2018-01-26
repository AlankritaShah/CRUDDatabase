package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 19-01-2018.
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity {

    List<Thing> billList;
    SQLiteDatabase mDatabasebill;
    ListView listViewThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);

        listViewThings = (ListView) findViewById(R.id.listViewThings);
        billList = new ArrayList<>();

        //opening the database
        mDatabasebill = openOrCreateDatabase(MainActivity.DATABASE_NAME_BILL, MODE_PRIVATE, null);
        //   SQLiteDatabase db = this.getWritableDatabase();

        //this method will display the things in the list
        showThingsFromDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        switch (id) {
            case R.id.exitbill:
            {
                Intent intent = new Intent(this, SearchCustomer.class);
                Bundle args = new Bundle();
                args.putSerializable("billList",(Serializable) billList);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);

//                MainActivity.i++;
//                createNewBillTable();
//                finish();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

//    public void createNewBillTable() {
//      //  mDatabasebill.execSQL("DROP TABLE bills");
//
//        //creates the bill table in the billing database
//        mDatabasebill.execSQL(
//                "CREATE TABLE IF NOT EXISTS Bill" + MainActivity.i + "(\n" +
//                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                        "    name varchar(200) NOT NULL,\n" +
//                        "    unit varchar(200) NOT NULL,\n" +
//                        "    stock double NOT NULL,\n" +
//                        "    price double NOT NULL,\n" +
//                        "    barcodenumber varchar(200) NOT NULL\n" +
//                        ");"
//        );
//    }

    private void showThingsFromDatabase() {

        //fetches data from the billing table
        Cursor cursorThings = mDatabasebill.rawQuery("SELECT * FROM Bill" + MainActivity.i, null);

        //if the cursor has some data
        if (cursorThings.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the bill list
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
}