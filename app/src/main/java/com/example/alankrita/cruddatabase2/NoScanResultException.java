package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 01-02-2018.
 */
public class NoScanResultException extends Exception {
    public NoScanResultException() {}
    public NoScanResultException(String msg) { super(msg); }
    public NoScanResultException(Throwable cause) { super(cause); }
    public NoScanResultException(String msg, Throwable cause) { super(msg, cause); }
}