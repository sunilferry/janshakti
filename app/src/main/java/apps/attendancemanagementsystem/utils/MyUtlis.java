package apps.attendancemanagementsystem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.attendancemanagementsystem.R;

import static android.content.Context.WIFI_SERVICE;

public class MyUtlis {
    private Context context;
    private Snackbar snackbar;
    int IMG_WIDTH = 200;
    int IMG_HEIGHT = 200;

    public MyUtlis(Context context) {
        this.context = context;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static Drawable setVectorForPreLollipop(int resourceId, Context activity) {
        Drawable icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            icon = VectorDrawableCompat.create(activity.getResources(), resourceId, activity.getTheme());
        } else {
            icon = activity.getResources().getDrawable(resourceId, activity.getTheme());
        }
        return icon;
    }

    public static void setVectorForPreLollipop(TextView textView, int resourceId, Context activity, int position) {
        Drawable icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            icon = VectorDrawableCompat.create(activity.getResources(), resourceId,
                    activity.getTheme());
        } else {
            icon = activity.getResources().getDrawable(resourceId, activity.getTheme());
        }
        switch (position) {
            case 0: //LEFT
                textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                break;

            case 1://RIGHT
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                break;

            case 2://TOP
                textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                break;

            case 3://Bottom
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
                break;
        }
    }

    public void alretBox(String Message) {
        new AlertDialog.Builder(context)
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showSnackBar(String string, View linearLayout) {
        snackbar = Snackbar.make(linearLayout, string, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(context.getResources().getColor(R.color.toolbar_clr));
        snackbar.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidMobiel(String s) {
        Pattern p = Pattern.compile("(0/91)?[5-9][0-9]{9}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public byte[] compress(Bitmap bmp) {
        Log.e("myutlis_ompress", "compress: " + bmp);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }


    public String getMobileIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("mobile ip address", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("mobile ip address", ex.toString());
        }
        return null;
    }

    @SuppressLint("WifiManagerPotentialLeak")
    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    public String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);
        image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
