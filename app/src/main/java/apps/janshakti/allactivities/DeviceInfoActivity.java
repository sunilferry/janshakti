package apps.janshakti.allactivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import apps.janshakti.BuildConfig;
import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;

public class DeviceInfoActivity extends BaseActivity {
    private static final String TAG = "DeviceInfoActivity";

    TextView device_name_tv, model_tv, version_tv, latitude_tv, longitude_tv, speed_tv, app_version_tv;
    ImageView back_iv;

    double latitude, longitude;

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
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DeviceInfoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates2();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        initView();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "onLocationResult: " + location);

                    hideLoader();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latitude_tv.setText(String.valueOf(latitude));
                    longitude_tv.setText(String.valueOf(longitude));
                }
            }
        };


        startLocationUpdates2();


        setData();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setData() {
        device_name_tv.setText(new StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString());
        model_tv.setText(Build.MODEL);
        version_tv.setText(Build.VERSION.SDK_INT + " ");
        app_version_tv.setText(BuildConfig.VERSION_NAME);
    }

    void initView() {
        device_name_tv = findViewById(R.id.device_name_tv);
        model_tv = findViewById(R.id.model_tv);
        app_version_tv = findViewById(R.id.app_version_tv);
        back_iv = findViewById(R.id.back_iv);

        version_tv = findViewById(R.id.version_tv);
        latitude_tv = findViewById(R.id.latitude_tv);
        longitude_tv = findViewById(R.id.longitude_tv);
        speed_tv = findViewById(R.id.speed_tv);

    }
}