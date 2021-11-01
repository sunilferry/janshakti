package apps.janshakti.allactivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;

public class QrScannerActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "QrScannerActivity";
    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button skip_btn,scan_btn;
    TextView error_tv;
    ImageView back_iv;
    String intentData = "";
    Handler handler = new Handler(Looper.getMainLooper());

    double latitude = 0.0, longitude = 0.0,offlatitude = 0.0, offlongitude = 0.0,distnearest;



    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    LocationRequest locationRequest;

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(300000);
        locationRequest.setFastestInterval(15000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates2() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(QrScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestAllPermission();
            } else {
                showAlert(getString(R.string.permission_camera_rationale11), "permission");
            }

        } else {
            showLoader();
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates2() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates2();
        cameraSource.release();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates2();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        surfaceView = findViewById(R.id.surfaceView);
        skip_btn = findViewById(R.id.skip_btn);
        scan_btn = findViewById(R.id.scan_btn);
        error_tv = findViewById(R.id.error_tv);
        back_iv = findViewById(R.id.back_iv);
        skip_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(960, 540)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        createSurface();
        createBarCodeDetector();


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                hideLoader();
                if (locationResult == null) {
                    startLocationUpdates2();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "onLocationResult: " + location);


                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        };

        startLocationUpdates2();




    }
    private void createBarCodeDetector(){
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    intentData = barcodes.valueAt(0).displayValue;
                    Log.d(TAG, "receiveDetections: " + intentData);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String officeLatLngs[]=intentData.split(",");
                            if(officeLatLngs.length==2){
                                try {
                                    offlatitude=Double.parseDouble(officeLatLngs[0].trim());
                                    offlongitude=Double.parseDouble(officeLatLngs[1].trim());
                                    findNearest();

                                }catch (Exception e){
                                    error_tv.setText(R.string.invalid_qr);
                                    toast(getString(R.string.invalid_qr));
                                    scan_btn.setVisibility(View.VISIBLE);
                                }
                            }else {
                                toast(getString(R.string.invalid_qr));
                                error_tv.setText(R.string.invalid_qr);
                                scan_btn.setVisibility(View.VISIBLE);
                            }
                            cameraSource.release();
                        }
                    });


                }
            }
        });
    }

    private void createSurface(){

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QrScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QrScannerActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }


    private void findNearest() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distnearest = distance(latitude, longitude,offlatitude,offlongitude);

                if (distnearest < 100) {
                    gotoActivity(PunchAttendanceActivity.class);
                    finish();
                } else {
                    error_tv.setText(R.string.out_of_office);
                    toast(getString(R.string.out_of_office));
                    scan_btn.setVisibility(View.VISIBLE);
                }

            }
        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_btn:
                gotoActivity(PunchAttendanceActivity.class);
                finish();
                break;
            case R.id.scan_btn:
                try {
                    scan_btn.setVisibility(View.GONE);
                    barcodeDetector = new BarcodeDetector.Builder(this)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();

                    cameraSource = new CameraSource.Builder(this, barcodeDetector)
                            .setRequestedPreviewSize(960, 540)
                            .setAutoFocusEnabled(true) //you should add this feature
                            .build();
                    cameraSource.start(surfaceView.getHolder());


                    createSurface();
                    createBarCodeDetector();
                }catch (Exception e){

                }
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }
}