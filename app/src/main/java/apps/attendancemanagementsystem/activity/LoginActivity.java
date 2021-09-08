package apps.attendancemanagementsystem.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import apps.attendancemanagementsystem.R;
import apps.attendancemanagementsystem.allactivities.Activity_MainDashboard;
import apps.attendancemanagementsystem.utils.AppSession;
import apps.attendancemanagementsystem.utils.Cons;
import apps.attendancemanagementsystem.utils.PermissionUtil;
import apps.attendancemanagementsystem.utils.Utilities;
import apps.attendancemanagementsystem.utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout til_email, til_pass;
    private AppCompatEditText et_email, et_password;
    private Utilities utilities;
    private AppSession appSession;
    private AppCompatButton btn_login;
    private TextView btn_forgot_password;
    private String userNameet, passwordet;
    String access_token = "", token_type = "", expires_in = "", username = "", nameofMandi = "", mandiUniqueId = "", addressofMandi = "",
            latitude = "", longitude = "", employeeUserId = "",mobile="",email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login_demo);
        utilities = Utilities.getInstance(this);
        appSession = new AppSession(this);
        checkForMarshmellowPermission();
        init();
    }

    private void init() {
        this.btn_forgot_password = findViewById(R.id.btn_forgot_password);
        this.til_email = findViewById(R.id.til_email);
        this.til_pass = findViewById(R.id.til_pass);
        this.et_email = findViewById(R.id.et_email);
        this.et_password = findViewById(R.id.et_password);
        this.btn_login = findViewById(R.id.btn_login);
//        this.ll_signup = findViewById(R.id.ll_signup);

        btn_login.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
//        ll_signup.setOnClickListener(this);
        et_email.addTextChangedListener(new GenericTextWatcher(et_email));
        et_password.addTextChangedListener(new GenericTextWatcher(et_password));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            userNameet = Objects.requireNonNull(et_email.getText()).toString();
            passwordet = MD5(Objects.requireNonNull(et_password.getText()).toString());
            if (isValid()) {
                if (Utilities.getInstance(this).isNetworkAvailable()) {
                    getLogin();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("No internet connection found");
                    builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    builder.create();
                    builder.show();
                  //  Toast.makeText(getApplicationContext(), "No internet connection found", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (v.getId() == R.id.btn_forgot_password) {
            Intent frgtPassword = new Intent(LoginActivity.this, Activity_ForgotPassword.class);
            startActivity(frgtPassword);
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_email:
                    til_email.setError(null);
                    break;
                case R.id.et_password:
                    til_pass.setError(null);
                    break;
            }
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        String email = Objects.requireNonNull(et_email.getText()).toString().trim();
        String password = Objects.requireNonNull(et_password.getText()).toString().trim();
        if (TextUtils.isEmpty(email)) {
            til_email.setError("Can't Empty");
            isValid = false;
        } else {
            til_email.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            til_pass.setError("Can't Empty");
            isValid = false;
        } else {
            til_pass.setError(null);
        }
        return isValid;
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }

    private void getLogin() {
        String url = Cons.serverUrl+"UserAuthentication";
        final ProgressDialog pDialog = ProgressDialog.show(LoginActivity.this, null, null);
        pDialog.setContentView(R.layout.progress_loader);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e("response", "response" + response);
                    pDialog.dismiss();
                    if (response.contains("access_token")) {
                        appSession.setLogin(true);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            access_token = jsonObject.getString("access_token");
                            token_type = jsonObject.getString("token_type");
                            expires_in = jsonObject.getString("expires_in");

                            username = jsonObject.getString("Username");
                            appSession.setUsername(username);

                            nameofMandi = jsonObject.getString("NameofMandi");
                            appSession.setNameofMandi(nameofMandi);

                            mandiUniqueId = jsonObject.getString("MandiUniqueId");
                            appSession.setMandiUniqueId(mandiUniqueId);

                            addressofMandi = jsonObject.getString("AddressofMandi");
                            appSession.setAddressofMandi(addressofMandi);

                            //for EmployeeUserId
                            employeeUserId = jsonObject.getString("EmployeeId");
                            appSession.setEmployeeUserId(employeeUserId);

                            //for Mobile number
                            mobile = jsonObject.getString("Mobile");
                            appSession.setMobileNumber(mobile);

                            //for email address
                            email = jsonObject.getString("Email");
                            appSession.setEmailAddress(email);

                            long expriy_time = System.currentTimeMillis() + (Integer.parseInt(expires_in) * 1000);
                            System.out.println("currenttime" + System.currentTimeMillis());
                            System.out.println("expirytime" + expriy_time);
                            System.out.println("server_expire_time" + expires_in);

                            appSession.setLogin(true);
                            appSession.setAccessToken(access_token);
                            Intent i = new Intent(getApplicationContext(), Activity_MainDashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getResources().getString(R.string.incorrect_login));
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                finish();
                            });
                            builder.create();
                            builder.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    pDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
//                                pDialog.dismiss();
                            //callTask(1);
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";

                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = " Please do login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = " Something is getting wrong";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (errorMessage.contains("Check your inputs")) {
                        showFailedDialog("Invalid credentials");
                    } else {
                        utilities.dialogOK(LoginActivity.this, getResources().getString(R.string.app_name), errorMessage, "OK", false);
                    }
                    error.printStackTrace();
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userNameet);
                params.put("password", passwordet);
                params.put("grant_type", "password");
                return params;
            }
        };
        int socketTimeout = 30 * 1000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes(StandardCharsets.UTF_8));
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


    public void checkForMarshmellowPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestAllPermission();
        }
    }

    public void requestAllPermission() {
        Log.e("TAG", "CAMERA permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.e("TAG", "Displaying camera permission rationale to provide additional context.");
            Utils.createSimpleDialog1(this, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11),
                    getString(R.string.reqst_permission),
                    () -> ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            ASK_MULTIPLE_PERMISSION_REQUEST_CODE));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    protected static final int WRITE_STORAGE = 102;
    protected static final int READ_STORAGE = 101;
    protected static final int REQUEST_CAMERA = 0;
    protected static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 11;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            Log.e("TAG", "Received response for contact permissions request.");
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Log.e("print_me", "print_me");
            } else {
                Log.e("TAG", "Contacts permissions were NOT granted.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showFailedDialog(String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(status);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create();
        builder.show();
    }
}