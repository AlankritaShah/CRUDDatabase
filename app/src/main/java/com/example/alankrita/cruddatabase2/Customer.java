package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 24-01-2018.
 */
public class Customer {
    String name, phone;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}