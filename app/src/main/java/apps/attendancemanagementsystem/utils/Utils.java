package apps.attendancemanagementsystem.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String setLeadingZero(int no) {
        String str = "" + no;
        if (no < 10)
            str = "0" + no;
        return str.trim();
    }

    public static void setPrefrence(Context context, String key, String value) {
        SharedPreferences prefrence = context.getSharedPreferences("TipZilla", 0);
        Editor editor = prefrence.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPrefrenceTab(Context context, String key, String value) {
        SharedPreferences prefrence = context.getSharedPreferences("TipZilla", 0);
        Editor editor = prefrence.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPrefrenceTab(Context context, String key) {
        SharedPreferences prefrence = context.getSharedPreferences("TipZilla", 0);
        String data = prefrence.getString(key, "-1");
        return data;
    }

    public static void checkcloseactivity(Activity con) {
        if (con != null && !con.isFinishing()) {
            con.finish();
        }
    }

    public static String getPrefrence(Context context, String key) {
        SharedPreferences prefrence = context.getSharedPreferences("TipZilla", 0);
        String data = prefrence.getString(key, "");
        return data;
    }

    public static void dumpIntent(Intent i) {
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Log.e("Intent Data", "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }

    public static Dialog createSimpleDialog1(Context context, String title, String msg, String btnLabel1, final Method method1) {
        String chars = title;
        SpannableString str = new SpannableString(chars);
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#199D59")), 0,
                chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setMessage(msg);

        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (method1 != null)
                            method1.execute();
                    }
                });

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        try {
            divider.setBackgroundColor(Color.parseColor("#199D59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Dialog createSimpleDialog(Context context, String title, String msg, String btnLabel1, String btnLabel2,
                                            final Method method1, final Method method2) {
        String chars = title;
        SpannableString str = new SpannableString(chars);
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#aaa")), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setMessage(msg);
        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (method1 != null)
                            method1.execute();
                    }
                });

        builder.setNegativeButton(btnLabel2,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (method2 != null)
                            method2.execute();
                    }
                });

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(Color.parseColor("#aaa"));
        return d;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Error | Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Dialog createSimpleDialog(Context context, String title,
                                            String msg, String btnLabel1, String btnLabel2, final Method method1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (method1 != null)
                            method1.execute();
                    }
                });

        builder.setNegativeButton(btnLabel2,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // if (method2 != null)
                        // method2.execute();
                    }
                });

        return builder.show();
    }

    public static Dialog createSimpleDialog(Context context, String title, String msg, String btnLabel, final Method method) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btnLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        method.execute();
                    }
                });

        return builder.create();
    }

    public static Dialog createInfoDialog(Context context, String title, String msg, String buttontext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(buttontext,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void showToastS(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean checkNetworkConnection(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public interface Method {
        void execute();
    }

    public static boolean isTabletDevice(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return xlarge || large;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View v = activity.getCurrentFocus();
                if (v != null) {
                    IBinder binder = activity.getCurrentFocus()
                            .getWindowToken();
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0);
                    }
                }
            }
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }
}
