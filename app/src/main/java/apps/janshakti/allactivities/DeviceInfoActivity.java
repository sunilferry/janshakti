package apps.janshakti.allactivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import apps.janshakti.BuildConfig;
import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;

public class DeviceInfoActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {
    private static final String TAG = "DeviceInfoActivity";

    TextView device_name_tv, model_tv, version_tv, latitude_tv, longitude_tv, speed_tv, app_version_tv;
    ImageView back_iv;

    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;  /* 15 secs */
    private long FASTEST_INTERVAL = 500; /* 5 secs */
    private LocationManager locationManager;
    int checkLocation = 0;
    double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        initView();
        setData();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocation = 0;
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    private void getLocation() {
        showLoader();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }


    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(@NotNull Location location) {
        if (checkLocation != 2) {
            checkLocation++;
            hideLoader();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latitude_tv.setText(String.valueOf(latitude));
            longitude_tv.setText(String.valueOf(longitude));

            mGoogleApiClient.disconnect();
            if (checkLocation == 2) {
                locationManager.removeUpdates(DeviceInfoActivity.this);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(DeviceInfoActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeviceInfoActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, DeviceInfoActivity.this);
                }
            }, 200);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, DeviceInfoActivity.this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            Log.d(TAG, "startLocationUpdates: " + e.getLocalizedMessage());
        }

    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
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