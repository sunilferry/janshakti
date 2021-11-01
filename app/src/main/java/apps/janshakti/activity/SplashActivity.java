package apps.janshakti.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import apps.janshakti.R;
import apps.janshakti.allactivities.MainActivity;

public class SplashActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            if (isConnected()) {
                if (appSession.isLogin()) {
                    gotoActivityWithFinish(MainActivity.class);
                } else {
                    gotoActivityWithFinish(LoginActivity.class);
                }
            } else {
                showAlert(getString(R.string.no_internet), "");
            }
        }, 2000);
    }
}