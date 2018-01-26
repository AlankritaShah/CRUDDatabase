package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 16-01-2018.
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

public class ThingAdapter extends ArrayAdapter<Thing> {

    Context mCtx;
    int listLayoutRes;
    List<Thing> thingList;
    SQLiteDatabase mDatabase;

    public ThingAdapter(Context mCtx, int listLayoutRes, List<Thing> thingList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, thingList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.thingList = thingList;
        this.mDatabase = mDatabase;
       // this.mDatabaseCT = mDatabaseCT;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

       final Thing thing = thingList.get(position);
//        Log.i("position haha", String.valueOf(position));
//        Log.i("id haha", String.valueOf(thing.getId()));

        TextView textViewName =(TextView) view.findViewById(R.id.textViewName);
        TextView textViewUt =(TextView) view.findViewById(R.id.textViewUnit);
        TextView textViewStock =(TextView) view.findViewById(R.id.textViewStock);
        TextView textViewPrice =(TextView) view.findViewById(R.id.textViewPrice);


        textViewName.setText(thing.getName());
        textViewUt.setText(thing.getUt());
        textViewStock.setText(String.valueOf(thing.getStock()));
        textViewPrice.setText(String.valueOf(thing.getPrice()));

        Button buttonDelete = (Button) view.findViewById(R.id.buttonDeleteThing);
        Button buttonEdit =(Button) view.findViewById(R.id.buttonEditThing);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateThing(thing);
              //  Log.i("thing",thing.getName());
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
                        String sql = "DELETE FROM things WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{thing.getId()});
                        reloadThingsFromDatabase();

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
    private void updateThing(final Thing thing) {
       // SQLiteDatabase db = this.getWritableDatabase();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_thing, null);
        builder.setView(view);

        final EditText editTextName = (EditText) view.findViewById(R.id.editTextName);
        final EditText editTextStock = (EditText) view.findViewById(R.id.editTextStock);
        final EditText editTextPrice = (EditText) view.findViewById(R.id.editTextPrice);
        final Spinner spinnerUnit = (Spinner) view.findViewById(R.id.spinnerUnit);

        editTextName.setText(thing.getName());
        editTextStock.setText(String.valueOf(thing.getStock()));
        editTextPrice.setText(String.valueOf(thing.getPrice()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateThing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String stock = editTextStock.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                String ut = spinnerUnit.getSelectedItem().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (stock.isEmpty()) {
                    editTextStock.setError("Stock can't be blank");
                    editTextStock.requestFocus();
                    return;
                }

                if (price.isEmpty()) {
                    editTextPrice.setError("Price can't be blank");
                    editTextPrice.requestFocus();
                    return;
                }

//                mDatabase.execSQL("UPDATE things \n" +
//                        "SET name = ?, \n" +
//                        "unit = ?, \n" +
//                        "stock = ?, \n" +
//                        "price = ? \n" +
//                        "WHERE id = ?;\n", new String[]{name, ut, stock, price, String.valueOf(thing.getId())});
//                Log.i("name",name);


              //  SQLiteDatabase db = this.getWritableDatabase();
                Log.i("id",String.valueOf(thing.getId()));
                ContentValues cv = new ContentValues();
                cv.put("name",name);
                cv.put("unit",ut);
                cv.put("stock",stock);
                cv.put("price",price);
                mDatabase.update("things", cv, "id=" + String.valueOf(thing.getId()), null);
//                Log.i("name after cv",name);
//                Cursor abc = mDatabase.rawQuery("SELECT * FROM things", null);
//                Log.i("db",abc.toString());

                Toast.makeText(mCtx, "Item Updated", Toast.LENGTH_SHORT).show();
                reloadThingsFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadThingsFromDatabase() {
        Cursor cursorThings = mDatabase.rawQuery("SELECT * FROM things", null);
        if (cursorThings.moveToFirst()) {
            thingList.clear();
            do {
                thingList.add(new Thing(
                        cursorThings.getInt(0),
                        cursorThings.getString(1),
                        cursorThings.getString(2),
                        cursorThings.getDouble(3),
                        cursorThings.getDouble(4),
                        cursorThings.getString(5)
                ));
              //  Log.i("cursorname", cursorThings.getString(1));
            } while (cursorThings.moveToNext());

        }
      //  Log.i("cursorThings", cursorThings.getString(1));
        cursorThings.close();
        notifyDataSetChanged();
    }
}