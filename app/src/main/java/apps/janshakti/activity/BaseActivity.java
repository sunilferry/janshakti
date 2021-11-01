package apps.janshakti.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import apps.janshakti.BuildConfig;
import apps.janshakti.R;
import apps.janshakti.database.LocationDatabase;
import apps.janshakti.networking.WebApiCalls;
import apps.janshakti.utils.AppSession;
import apps.janshakti.utils.Utils;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected WebApiCalls webApiCalls;
    protected AppSession appSession;
    Dialog loader, successDialog;
    TextView title_tv, msg_tv;
    ImageView icon_iv;
    protected LocationDatabase database = null;

    LinearLayout ok;
    AlertDialog.Builder builder = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (webApiCalls == null) {
            webApiCalls = new WebApiCalls();
            appSession = new AppSession(BaseActivity.this);
        }

        if (loader == null) {
            loader = new Dialog(BaseActivity.this);
            loader.setContentView(R.layout.custom_loader);
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (database == null) {
            database = LocationDatabase.getInstance(this);
        }

        if (successDialog == null) {
            successDialog = new Dialog(BaseActivity.this);
            successDialog.setContentView(R.layout.show_reading_success);
            successDialog.setCanceledOnTouchOutside(false);
            successDialog.setCancelable(false);
            ok = successDialog.findViewById(R.id.ok);
            title_tv = successDialog.findViewById(R.id.title_tv);
            msg_tv = successDialog.findViewById(R.id.msg_tv);
            icon_iv = successDialog.findViewById(R.id.icon_iv);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (successDialog != null && successDialog.isShowing()) {
                        successDialog.dismiss();
                    }
                } catch (Exception e) {

                }

            }
        });

    }


    protected void showLogoutAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage("Do you want to Logout from the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    appSession.setImage("");
                    appSession.setLogin(false);
                    gotoActivityWithFinish(LoginActivity.class);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void showLoader() {
        try {
            if (loader != null && !loader.isShowing()) {
                loader.show();
            }
        } catch (Exception e) {

        }

    }

    protected void hideLoader() {
        try {
            if (loader != null && loader.isShowing()) {
                loader.dismiss();
            }
        } catch (Exception e) {

        }
    }

    protected void toast(String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }

    }

    protected void gotoActivity(Class<?> actviity) {
        Intent myIntent = new Intent(BaseActivity.this, actviity);
        startActivity(myIntent);
    }

    protected void gotoActivityWithFinish(Class<?> actviity) {
        try {
            Intent myIntent = new Intent(BaseActivity.this, actviity);
            startActivity(myIntent);
            finishAffinity();
        } catch (Exception e) {

        }

    }

    protected void showSuccess() {
        try {
            if (successDialog != null && !successDialog.isShowing()) {
                icon_iv.setImageResource(R.drawable.success_img);
                title_tv.setText("Attendance Done");
                msg_tv.setText("Your attendance has been uploaded successfully on server");
                successDialog.show();
            }
        } catch (Exception e) {

        }

    }

    protected void showFailed(String message) {
        try {
            if (successDialog != null && !successDialog.isShowing()) {
                icon_iv.setImageResource(R.drawable.failed);
                title_tv.setText("Attendance Failed");
                msg_tv.setText(message);
                successDialog.show();
            }
        } catch (Exception e) {

        }
    }

    protected void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Attendance Management System Mobile App");
            String shareMessage = "\nLet me recommend you this application for Attendance Management System Mobile App.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void rate_app() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }


    protected float distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = toRadians(lat2 - lat1);
        double dLng = toRadians(lng2 - lng1);
        double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        return dist;
    }

    protected String base64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    protected boolean isConnected() {
        boolean connected;
        try {

            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            return connected;

        } catch (Exception e) {
            return false;
        }

    }


    protected String encrypt(String value) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void requestAllPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Utils.createSimpleDialog1(this, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11),
                    getString(R.string.reqst_permission),
                    () -> ActivityCompat.requestPermissions(this, new String[]{

                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            ASK_MULTIPLE_PERMISSION_REQUEST_CODE));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{

                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);


        }
    }

    protected void openSetting() {
        try {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);
        } catch (Exception e) {

        }

    }

    protected static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 11;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void showAlert(String msg, String type) {
        try {
            if (builder == null) {
                builder = new AlertDialog.Builder(BaseActivity.this);
            }
            builder.setMessage(msg);
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                if (type.equals("permission")) {
                    openSetting();
                }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {

        }
    }

    protected String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            return "";
        }
        return "";
    }


    protected String getCompleteAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                StringBuilder strReturnedAddress = new StringBuilder("");
                strReturnedAddress.append(addresses.get(0).getAddressLine(0)); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                Log.d(TAG, "getCompleteAddress: " + addresses.get(0).getLocality());
                Log.d(TAG, "getCompleteAddress: " + addresses.get(0).getSubLocality());

                strAdd = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                Log.w(TAG, "No Address returned!");
                return "Unknown Address";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
            return "Unknown Address";
        }
        return strAdd;
    }

    public boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    protected void buildAlertMessageNoGps() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.dismiss();
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {

        }

    }


}
