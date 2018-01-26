package com.example.alankrita.cruddatabase2;

import java.io.Serializable;

/**
 * Created by ALANKRITA on 16-01-2018.
 */
public class Thing implements Serializable {
    int id;
    String name, ut, barcodenumber;
    double stock, price;

    public Thing(int id, String name, String ut, double stock, double price, String barcodenumber) {
        this.id = id;
        this.name = name;
        this.ut = ut;
        this.stock = stock;
        this.price = price;
        this.barcodenumber=barcodenumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUt() {
        return ut;
    }

    public double getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public String getbarcodenumber() {
        return barcodenumber;
    }
}