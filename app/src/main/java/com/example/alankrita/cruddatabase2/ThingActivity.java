package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 16-01-2018.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ThingActivity extends AppCompatActivity {

    List<Thing> thingList;
    SQLiteDatabase mDatabase, mDatabaseCT;
    ListView listViewThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);

        listViewThings = (ListView) findViewById(R.id.listViewThings);
        thingList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        mDatabaseCT = openOrCreateDatabase(MainActivity.DATABASE_CUSTOMER_THING, MODE_PRIVATE, null);

        //   SQLiteDatabase db = this.getWritableDatabase();

        //this method will display the things in the list
        showThingsFromDatabase();
    }

    private void showThingsFromDatabase() {

        //fetching all the things
        Cursor cursorThings = mDatabase.rawQuery("SELECT * FROM things", null);

        //if the cursor has some data
        if (cursorThings.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the thing list
                thingList.add(new Thing(
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

        listViewThings.setAdapter(new ThingAdapter(this, R.layout.list_layout_thing, thingList, mDatabase));
    }
}