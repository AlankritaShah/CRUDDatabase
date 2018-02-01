package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 01-02-2018.
 */
public interface ScanResultReceiver {
    /**
     * function to receive scanresult
     * @param codeFormat format of the barcode scanned
     * @param codeContent data of the barcode scanned
     */
    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}
