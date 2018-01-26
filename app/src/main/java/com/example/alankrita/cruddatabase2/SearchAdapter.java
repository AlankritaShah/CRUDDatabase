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

public class SearchAdapter extends ArrayAdapter<Thing> {

    Context mCtx;
    int listLayoutRes;
    List<Thing> billList;
    SQLiteDatabase mDatabasebill;

    public SearchAdapter(Context mCtx, int listLayoutRes, List<Thing> billList, SQLiteDatabase mDatabasebill) {
        super(mCtx, listLayoutRes, billList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.billList = billList;
        this.mDatabasebill = mDatabasebill;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Thing thing = billList.get(position);

        TextView textViewName =(TextView) view.findViewById(R.id.textViewName);
        TextView textViewUt =(TextView) view.findViewById(R.id.textViewUnit);
        TextView textViewStock =(TextView) view.findViewById(R.id.textViewStock);
        TextView textViewPrice =(TextView) view.findViewById(R.id.textViewPrice);


        textViewName.setText(thing.getName());
        textViewUt.setText(thing.getUt());
        textViewStock.setText(String.valueOf(thing.getStock()));
        textViewPrice.setText(String.valueOf(thing.getPrice()));

        return view;
    }
    }