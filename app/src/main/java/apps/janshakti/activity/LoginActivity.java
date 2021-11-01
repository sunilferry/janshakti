package apps.janshakti.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.util.Objects;

import apps.janshakti.R;
import apps.janshakti.allactivities.DeviceInfoActivity;
import apps.janshakti.allactivities.LocationInfoActivity;
import apps.janshakti.allactivities.MainActivity;
import apps.janshakti.callbacks.LoginCallback;
import apps.janshakti.model.login_model.LoginResponse;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginCallback {
    private static final String TAG = "LoginActivity";
    private TextInputLayout til_email, til_pass;
    private AppCompatEditText et_email, et_password;
    private Button btn_login;
    LinearLayout location_ll;
    private TextView btn_forgot_password;
    private String userNameet, passwordet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlert(getString(R.string.permission_camera_rationale11), "permission");
        } else {
            requestAllPermission();
        }
        init();
    }

    private void init() {
        btn_forgot_password = findViewById(R.id.btn_forgot_password);
        til_email = findViewById(R.id.til_email);
        til_pass = findViewById(R.id.til_pass);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        location_ll = findViewById(R.id.location_ll);

        btn_login.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
        location_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            userNameet = Objects.requireNonNull(et_email.getText()).toString();
            passwordet = encrypt(Objects.requireNonNull(et_password.getText()).toString());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showAlert(getString(R.string.permission_camera_rationale11), "permission");
                } else {
                    requestAllPermission();
                }
            } else {
                if (isValid()) {
                    if (isConnected()) {
                        showLoader();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("email", userNameet);
                        jsonObject.addProperty("password", passwordet);
                        webApiCalls.login(this, jsonObject);
                    } else {
                        showAlert(getString(R.string.no_internet), "");
                    }
                }
            }
        } else if (v.getId() == R.id.btn_forgot_password) {
            gotoActivity(ForgotPasswordActivity.class);
        }else if (v.getId() == R.id.location_ll) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showAlert(getString(R.string.permission_camera_rationale11), "permission");
                } else {
                    requestAllPermission();
                }
            } else if (isConnected()){
                gotoActivity(LocationInfoActivity.class);

            }else {
                showAlert(getString(R.string.no_internet), "");
            }

        }
    }

    @Override
    public void onLoginResponse(LoginResponse loginResponse) {
        try {
            hideLoader();
            if (loginResponse.isStatus()) {
                appSession.setLogin(true);
                appSession.setAccessToken(loginResponse.getData().getResponse().getJwtToken());
                appSession.setUsername(loginResponse.getData().getResponse().getUserName());
                gotoActivityWithFinish(MainActivity.class);
            }
        } catch (Exception e) {
            toast(getString(R.string.incorrect_login));
        }
    }

    @Override
    public void onLoginFailed() {
        toast(getString(R.string.incorrect_login));
        hideLoader();
    }

    private boolean isValid() {
        boolean isValid = false;
        String email = Objects.requireNonNull(et_email.getText()).toString().trim();
        String password = Objects.requireNonNull(et_password.getText()).toString().trim();
        if (TextUtils.isEmpty(email)) {
            til_email.setError(getString(R.string.can_not_empty));
        } else if (TextUtils.isEmpty(password)) {
            til_pass.setError(getString(R.string.can_not_empty));
        } else {
            isValid = true;
        }
        return isValid;
    }
}