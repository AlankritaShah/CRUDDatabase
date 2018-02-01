package com.example.alankrita.cruddatabase2;

/**
 * Created by ALANKRITA on 31-01-2018.
 */
import android.content.Intent;
import android.graphics.Camera;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScannedResultActivity extends ActionBarActivity implements SurfaceHolder.Callback {
    private TextView formatTxt, contentTxt;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    boolean mPreviewRunning;
    Button btncapture;
    private IntentIntegrator qrScan;
    CaptureActivity ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scan);

        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        btncapture=(Button) findViewById(R.id.btncapture);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        qrScan = new IntentIntegrator(this);
        ca = new CaptureActivity();

        btncapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //take picture here
              //  mCamera.takePicture(null, null, mPictureCallback);

             //   ca.onWindowAttributesChanged();

                qrScan.setOrientationLocked(false);
//
                qrScan.initiateScan();
            }
        });


    }

  //  Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                Log.i("result",result.toString());
                String barcodenumber = result.getContents();

                Intent in = new Intent(this, SearchActivity.class);
                in.putExtra("barcodenumber",barcodenumber);
                startActivity(in);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] imageData, Camera c) {
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData , 0, imageData .length);
//            String file_path=saveToInternalSorage(bitmap);
//            Toast.makeText(getApplicationContext(),"Image stored succesfully at "+file_path,Toast.LENGTH_LONG).show();
//        }
//    };

//    private String saveToInternalSorage(Bitmap bitmapImage){
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,"marina1.jpg");
//
//        FileOutputStream fos = null;
//        try {
//
//            fos = new FileOutputStream(mypath);
//
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return directory.getAbsolutePath();
//    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        qrScan.setOrientationLocked(false);
////
//                qrScan.initiateScan();
//        qrScan.setOrientationLocked(false);
////
//        qrScan.initiateScan();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
//        if (mPreviewRunning) {
//            mCamera.stopPreview();
//        }
//        Camera.Parameters p = mCamera.getParameters();
//        p.setPreviewSize(w, h);
//        mCamera.setParameters(p);
//        try {
//            mCamera.setPreviewDisplay(holder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mCamera.startPreview();
//        mPreviewRunning = true;

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        mCamera.stopPreview();
//        mPreviewRunning = false;
//        mCamera.release();

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * event handler for scan button
//     * @param view view of the activity
//     */
//    public void scanNow(View view){
//        // add fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ScanFragment scanFragment = new ScanFragment();
//        fragmentTransaction.add(R.id.scan_fragment,scanFragment);
//        fragmentTransaction.commit();
//    }
//
//    @Override
//    public void scanResultData(String codeFormat, String codeContent){
//        // display it on screen
//        formatTxt.setText("FORMAT: " + codeFormat);
//        contentTxt.setText("CONTENT: " + codeContent);
//    }
//
//    @Override
//    public void scanResultData(NoScanResultException noScanData) {
//        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
//        toast.show();
//    }
}