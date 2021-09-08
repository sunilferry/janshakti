package apps.attendancemanagementsystem.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import apps.attendancemanagementsystem.R;
import apps.attendancemanagementsystem.allactivities.Activity_MainDashboard;
import apps.attendancemanagementsystem.utils.AppSession;
import apps.attendancemanagementsystem.utils.AppVersion;
import apps.attendancemanagementsystem.utils.Utilities;

public class SplashActivity extends AppCompatActivity {
    AppSession appSession;
    private String result = "", version = "";
    TextView tv_version;
    Dialog FirstTime_myDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        appSession = new AppSession(this);
        tv_version = findViewById(R.id.version_txt);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText("App Version " + version);
        if (Utilities.getInstance(this).isNetworkAvailable()) {
            AppVersion job = new AppVersion();
            try {
                result = job.execute().get();
                Log.e("RESULT upmandiApp", " APP VERSION " + result);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("BUILD upmandiApp", " APP VERSION " + version);
        }

        new Handler().postDelayed(() -> {
            if (result != null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase(version)) {
                if (Utilities.getInstance(this).isNetworkAvailable()) {
                    showVersionAlert();
                    appSession.setLogin(false);
                    appSession.setUsername(null);
                    appSession.setNameofMandi(null);
                    appSession.setMandiUniqueId(null);
                    appSession.setAddressofMandi(null);
                    appSession.setEmployeeUserId(null);
                    appSession.setAccessToken(null);
                    appSession.setIP_address(null);
                }
            } else {
                if (appSession.isLogin()) {
                    Intent intent = new Intent(getApplicationContext(), Activity_MainDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

    private void showVersionAlert() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("App Update");
            builder.setMessage("A newer version of App is available.\nPlease update");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialog, which) -> {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void showVersionAlert() {
//        MaterialTextView btn_save;
//        FirstTime_myDialog = new Dialog(SplashActivity.this);
//        FirstTime_myDialog.setContentView(R.layout.alert_for_updation);
//        FirstTime_myDialog.setCanceledOnTouchOutside(false);
//        FirstTime_myDialog.setCancelable(false);
//        Objects.requireNonNull(FirstTime_myDialog.getWindow()).getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
//        btn_save = FirstTime_myDialog.findViewById(R.id.btn_save);
//
//        btn_save.setOnClickListener(view -> {
//            appSession.setLogin(false);
//            appSession.setUsername(null);
//            appSession.setNameofMandi(null);
//            appSession.setMandiUniqueId(null);
//            appSession.setAddressofMandi(null);
//            appSession.setEmployeeUserId(null);
//            appSession.setAccessToken(null);
//
//            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//            }
//            FirstTime_myDialog.dismiss();
//
//        });
//        FirstTime_myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        FirstTime_myDialog.show();
////        try {
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle("App Update");
////            builder.setMessage("A newer version of App is available.\nPlease update");
////            builder.setCancelable(false);
////            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
////                    try {
////                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
////                    } catch (android.content.ActivityNotFoundException anfe) {
////                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
////                    }
////                    dialog.dismiss();
////                }
////            });
////            builder.create();
////            builder.show();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // Continue updates when resumed
    }
}