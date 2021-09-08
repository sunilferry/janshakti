package apps.attendancemanagementsystem.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import apps.attendancemanagementsystem.R;
import apps.attendancemanagementsystem.utils.AppSession;
import apps.attendancemanagementsystem.utils.AppSignatureHashHelper;
import apps.attendancemanagementsystem.utils.Cons;
import apps.attendancemanagementsystem.utils.OtpView;
import apps.attendancemanagementsystem.utils.Utilities;

public class Activity_ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private AppSession appSession;
    private Utilities utilities;
    private String OTP = "";
    @SuppressLint("StaticFieldLeak")
    private static OtpView mOtpView;
    @SuppressLint("StaticFieldLeak")
    private static EditText checkOTP;

    private CountDownTimer countDownTimer;
    private MaterialButton getOTP_btn, getverify_btn, submit_btn;
    private TextInputEditText etMobilenumber, etPassword, etConfirmPassword;
    private TextInputLayout txt_input_mobile, txt_input_password, txt_input_confirmpassword;

    private LinearLayout resend_layout, first_ll, verify_otp_ll, linear_all_fields, resend_otp_ll;
    private TextView mobile_txt, tx_otp_timer, resend_txt;
    private String error;
    private String mobileNumber = "", password = "";
    String psw = "";
    private ImageView ll_back;
    String useridshow;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        utilities = Utilities.getInstance(this);
        appSession = new AppSession(this);

        ll_back = findViewById(R.id.ll_back);
        etMobilenumber = findViewById(R.id.etMobilenumber);
        txt_input_mobile = findViewById(R.id.txt_input_mobile);

        //for all fields
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        txt_input_password = findViewById(R.id.txt_input_password);
        txt_input_confirmpassword = findViewById(R.id.txt_input_confirmpassword);

        getOTP_btn = findViewById(R.id.getOTP_btn);
        getverify_btn = findViewById(R.id.getverify_btn);
        submit_btn = findViewById(R.id.submit_btn);

        tx_otp_timer = findViewById(R.id.tx_otp_timer);
        mobile_txt = findViewById(R.id.mobile_txt);

        resend_layout = findViewById(R.id.resend_layout);
        first_ll = findViewById(R.id.first_ll);
        linear_all_fields = findViewById(R.id.linear_all_fields);
        verify_otp_ll = findViewById(R.id.verify_otp_ll);
        resend_otp_ll = findViewById(R.id.resend_otp_ll);

        mOtpView = findViewById(R.id.otp_view);
        checkOTP = findViewById(R.id.checkOTP);
        resend_txt = findViewById(R.id.resend_txt);

        resend_txt.setOnClickListener(this);
        getOTP_btn.setOnClickListener(this);
        getverify_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        ll_back.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 10) {
                    hideTheKeyboard(Activity_ForgotPassword.this, etMobilenumber);
                    txt_input_mobile.setErrorEnabled(false);
                    if (!validations()) {
                        Toast.makeText(Activity_ForgotPassword.this, error, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        etMobilenumber.addTextChangedListener(textWatcher);


        //for password
        TextWatcher textWatcher_pass = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() > 5) {
                    txt_input_password.setErrorEnabled(false);
                } else if (s.length() > 0 && s.length() < 6) {
                    txt_input_password.setErrorEnabled(true);
                    txt_input_password.setError("Password must be at least 6 characters");
                } else {
                    txt_input_password.setErrorEnabled(true);
                    txt_input_password.setError("Please enter password");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        etPassword.addTextChangedListener(textWatcher_pass);


        //for password
        TextWatcher textWatcher_confirmpass = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() > 5) {
                    txt_input_confirmpassword.setErrorEnabled(false);
                } else if (s.length() > 0 && s.length() < 6) {
                    txt_input_confirmpassword.setErrorEnabled(true);
                    txt_input_confirmpassword.setError("Password must be at least 6 characters");
                } else {
                    txt_input_confirmpassword.setErrorEnabled(true);
                    txt_input_confirmpassword.setError("Please enter password");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        etConfirmPassword.addTextChangedListener(textWatcher_confirmpass);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.getOTP_btn:
                if (validations()) {
                    if (Utilities.getInstance(this).isNetworkAvailable()) {
                        mobileNumber = etMobilenumber.getText().toString();
                        checkMobileNumberExistsornot(mobileNumber);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.resend_txt:
                try {
                    mobileNumber = etMobilenumber.getText().toString();
                    checkMobileNumberExistsornot(mobileNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.getverify_btn:
                if (mOtpView.equals(OTP)) {
                    linear_all_fields.setVisibility(View.VISIBLE);
                    first_ll.setVisibility(View.GONE);
                    stopCountDownTimer();
                    verify_otp_ll.setVisibility(View.GONE);
                    etMobilenumber.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "OTP doesn't matched", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.submit_btn:
                if (allfieldsverification()) {
                    psw = Objects.requireNonNull(etPassword.getText()).toString();
                    password = psw;
                    if (Utilities.getInstance(this).isNetworkAvailable()) {
                        postFieldsValue();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void checkMobileNumberExistsornot(String mobileNumber1) {
        String url = Cons.serverUrl + "api/Android/CheckMobile";
        final ProgressDialog pDialog = ProgressDialog.show(Activity_ForgotPassword.this, null, null);
        pDialog.setContentView(R.layout.progress_loader);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        JSONObject mainJson = new JSONObject();
        try {
            mainJson.put("Mobile", mobileNumber1);
            Log.e("finalsubmission", "final_submission" + mainJson.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String requestBody = mainJson.toString();

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e("response", "response" + response);
                    pDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String resultStatus = jsonObject.getString("ResultStatus");
                        if (resultStatus.equalsIgnoreCase("t")) {
                            useridshow = jsonObject.getString("UserId");
                            first_ll.setVisibility(View.VISIBLE);
                            resend_layout.setVisibility(View.VISIBLE);
                            tx_otp_timer.setVisibility(View.VISIBLE);
                            mobileNumber = Objects.requireNonNull(etMobilenumber.getText()).toString();
                            etMobilenumber.setEnabled(false);

                            startCountDownTimer();

                            SmsRetrieverClient client = SmsRetriever.getClient(this);
                            Task<Void> task = client.startSmsRetriever();
                            task.addOnSuccessListener(aVoid -> {
                                // Successfully started retriever, expect broadcast intent
                                // ...
                            });

                            task.addOnFailureListener(e -> {
                                // Failed to start retriever, inspect Exception for more details
                                // ...
                            });

                            OTP = createOtp();

                            AppSignatureHashHelper appSignatureHelper = new AppSignatureHashHelper(Activity_ForgotPassword.this);
                            String hashCode = appSignatureHelper.getAppSignatures().get(0);

                            ////  String hash = hashCode.replace("[", "");
                            // String hashF = hash.replace("]", "");
                            String msg = "%5b%23%5d%20Attendance%20ManagementSystem%20Mobile%20App%20verification%20code%20is%20" + OTP + "%20" + hashCode.replace("+", "%2b");
                            //String msg = "%5b%23%5d Van Mitra Mobile App verification code is " + OTP + " " + hashCode.replace("+", "%2b");
                            // String msg = "UP Nursery Mobile App verification code is " + OTP;
                            sendSms(msg, mobileNumber);
                            //String msg = "UP Nursery Mobile App verification code is " + OTP;

                            String number = mobileNumber;
                            String mask = number.replaceAll("\\w(?=\\w{4})", "X");
                            mobile_txt.setText("+91-" + mask);
                            initializeListeners();
                        } else {
                            utilities.dialogOK(Activity_ForgotPassword.this, getResources().getString(R.string.app_name), "Mobile Number doesn't exit",
                                    "OK", false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
//                    if (errorMessage.contains("Check your inputs")) {
//                        showFailedDialog("Invalid credentials");
//                    } else {
                    //utilities.dialogOK(LoginActivity.this, getResources().getString(R.string.app_name), errorMessage, "OK", false);
//                    }
                    error.printStackTrace();
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
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

    private boolean validations() {
        error = "";
        if (etMobilenumber.getText().toString().trim().equalsIgnoreCase("")) {
            txt_input_mobile.setErrorEnabled(true);
            txt_input_mobile.setError("Please enter mobile number");
            return false;
        }
        return true;
    }

    private boolean allfieldsverification() {
        if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            txt_input_password.setErrorEnabled(true);
            txt_input_password.setError("Please enter password");
            return false;
        } else if (etConfirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
//            error = "Please enter confirm password";
            txt_input_confirmpassword.setErrorEnabled(true);
            txt_input_confirmpassword.setError("Please enter confirm password");
            return false;
        }

        if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
            //error = Cons.match_password;
            txt_input_confirmpassword.setErrorEnabled(true);
            txt_input_confirmpassword.setError("Entered confirm password doesn't match with above password");
            return false;
        }
        return true;
    }

    public void sendSms(final String msg, String mobile) {
        //String Url = Cons.msgurl + "" + msg + "" + Cons.senderId + "" + mobile + "&smsContentType=Unicode";
        String Url = "http://mysms.msgclub.net/rest/services/sendSMS/sendGroupSms?AUTH_KEY=4d22efc7752a1f3c30c9c740e39969db&message=" + msg + "&senderId=SIGNUP&routeId=8&mobileNos=" + mobile + "&smsContentType=english";

        StringRequest getRequest = new StringRequest(Request.Method.GET, Url, response -> {
        },
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout!\nPlease try again.";
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
                                errorMessage = "Your session has expired!\nPlease login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = "Invalid Input!\n Please check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = " Something is getting wrong!\nPlease try again later";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    //showAlertDialogerror(errorMessage);
//                        utilities.dialogOK("");
                    error.printStackTrace();
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

//            @Override
////            public byte[] getBody() throws AuthFailureError {
////                try {
////                    return requestBody == null ? null : requestBody.getBytes("utf-8");
////                } catch (UnsupportedEncodingException uee) {
////                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
////                    return null;
////                }
//            }
        };
        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

    }

    public String createOtp() {
        long otpl = (Math.round(Math.random() * 8999) + 1000);
        Log.e("otpl>>>", "otpl>>>" + otpl);
        return Long.toString(otpl);
    }

    private void initializeListeners() {

        mOtpView.updateOtp(OTP);
        mOtpView.customTextChangeListener(checkOTP);

        checkOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (String.valueOf(s).equalsIgnoreCase(OTP)) {

                    Toast.makeText(getApplicationContext(), "OTP verified successfully", Toast.LENGTH_SHORT).show();
                    linear_all_fields.setVisibility(View.VISIBLE);
                    first_ll.setVisibility(View.GONE);
                    stopCountDownTimer();
                    verify_otp_ll.setVisibility(View.GONE);
                    etMobilenumber.setEnabled(false);

//                    if (Utilities.getInstance(Activity_Signup.this).isNetworkAvailable()) {
//                        //postDetails();
//                    } else {
//                        Toast.makeText(getApplicationContext(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                verify_otp_ll.setVisibility(View.GONE);
                getOTP_btn.setVisibility(View.GONE);
                //tx_otp_timer.setText("Resend OTP in " + " " + hmsTimeFormatter(millisUntilFinished));
                tx_otp_timer.setText(hmsTimeFormatter(millisUntilFinished));
//                resend_otp_ll.setVisibility(View.INVISIBLE);
                // progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                //tx_otp_timer.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                //tx_otp_timer.setText("Resend OTP in " + " " + hmsTimeFormatter(timeCountInMilliSeconds));
                resend_otp_ll.setVisibility(View.GONE);
                tx_otp_timer.setVisibility(View.GONE);
                resend_layout.setVisibility(View.VISIBLE);
                stopCountDownTimer();
            }
        }.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private String hmsTimeFormatter(long milliSeconds) {
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;
    }

    public static class MySMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                    Bundle extras = intent.getExtras();
                    Status status = (Status) Objects.requireNonNull(extras).get(SmsRetriever.EXTRA_STATUS);
                    switch (Objects.requireNonNull(status).getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:
                            // Get SMS message contents
                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                            // Extract one-time code from the message and complete verification
                            // by sending the code back to your server for SMS authenticity.
                            recivedSms(message);
                            break;

                        case CommonStatusCodes.TIMEOUT:
                            // Waiting for SMS timed out (5 minutes)
                            // Handle the error ...
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void recivedSms(String message) {
        try {
            mOtpView.setOTP(extractNumber(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String extractNumber(final String str) {
        if (str == null || str.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
                found = true;
            } else if (found) {
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }
        return sb.toString();
    }

    public void hideTheKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private void postFieldsValue() {
        String url = Cons.serverUrl + "api/Android/ResetEmployeePasswordMobile";
        final ProgressDialog pDialog = ProgressDialog.show(this, null, null);
        pDialog.setContentView(R.layout.progress_loader);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", useridshow);
            jsonObject.put("Password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("posting_forgot", "posting_forgot" +jsonObject.toString());
        final String requestBody = jsonObject.toString();
        StringRequest getRequest = new StringRequest(Request.Method.POST, url, response -> {
            pDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String resultStatus = jsonObject1.getString("ResultStatus");
                if (resultStatus.equalsIgnoreCase("t")) {
                    String resultMessage = jsonObject1.getString("ResultMessage");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Message");
                    builder.setMessage(resultMessage);
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        // Do nothing but close the dialog
                        Intent i = new Intent(Activity_ForgotPassword.this, LoginActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    utilities.dialogOK(Activity_ForgotPassword.this, getResources().getString(R.string.app_name), "Some technical error occurred", "OK", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout!\nPlease try again.";
                                //callTask(1);
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";
                                pDialog.dismiss();
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = "Your session has expired!\nPlease login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = "Invalid Input!\n Please check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = " Something is getting wrong!\nPlease try again later";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pDialog.dismiss();
                        }
                        //showAlertDialogerror(errorMessage);
//                        utilities.dialogOK("");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

    }

    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        return null;
    }

    private void showAlertDialog(String success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("App Update");
        builder.setMessage(success);
        builder.setPositiveButton("ठीक है", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        /*builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        builder.create();
        builder.show();
    }
}
