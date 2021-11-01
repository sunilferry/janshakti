package apps.janshakti.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import apps.janshakti.R;
import apps.janshakti.callbacks.OtpCallback;
import apps.janshakti.callbacks.UpdatePasswordCallback;
import apps.janshakti.callbacks.VerifyOtpCallback;
import apps.janshakti.model.OtpResponse;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, OtpCallback, UpdatePasswordCallback, VerifyOtpCallback {

    private static final String TAG = "Activity_ForgotPassword";

    private EditText mobile_et, otp_et,password_et, confirm_et;

    TextInputLayout til_pass,til_confirm;
    ImageView back_iv;
    TextView level_tv, title_tv, message_tv,second_tv;
    LinearLayout verify_ll,timer_ll;
    Button send_btn,resend_btn;

    private CountDownTimer countDownTimer;

    private String mobileNumber = "", password = "",otp="";


    int viewValue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initView();
    }

    void initView() {
        send_btn = findViewById(R.id.send_btn);
        mobile_et = findViewById(R.id.mobile_et);
        otp_et = findViewById(R.id.otp_et);
        password_et = findViewById(R.id.password_et);
        confirm_et = findViewById(R.id.confirm_et);
        back_iv = findViewById(R.id.back_iv);
        level_tv = findViewById(R.id.level_tv);
        title_tv = findViewById(R.id.title_tv);
        message_tv = findViewById(R.id.message_tv);
        verify_ll = findViewById(R.id.verify_ll);
        til_pass = findViewById(R.id.til_pass);
        til_confirm = findViewById(R.id.til_confirm);
        second_tv = findViewById(R.id.second_tv);
        resend_btn = findViewById(R.id.resend_btn);
        timer_ll = findViewById(R.id.timer_ll);

        back_iv.setOnClickListener(this::onClick);
        send_btn.setOnClickListener(this::onClick);
        resend_btn.setOnClickListener(this::onClick);

    }

    @Override
    public void onBackPressed() {
        if(viewValue==0||viewValue==3){
            finish();
        }else {
            viewValue=viewValue-1;
            handleView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                if(viewValue==0||viewValue==3){
                    finish();
                }else {
                    viewValue=viewValue-1;
                    handleView();
                }

                break;

                case R.id.resend_btn:

                    if(mobileNumber.length()==10){
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("userId",mobileNumber);
                        jsonObject.addProperty("cIPAddress",getIpAddress());
                       if(isConnected()){
                           viewValue=viewValue-1;
                           showLoader();
                           webApiCalls.sendOtp(this::onOtpResponse,jsonObject);
                       }
                    }else {
                        mobile_et.setError(getString(R.string.invalid_mobile));
                    }
                break;
            case R.id.send_btn:
                Log.d(TAG, "onClick: ");

                if(viewValue==0){
                    mobileNumber=mobile_et.getText().toString().trim();
                    if(mobileNumber.length()==10){
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("userId",mobileNumber);
                        jsonObject.addProperty("cIPAddress",getIpAddress());
                        showLoader();
                        webApiCalls.sendOtp(this::onOtpResponse,jsonObject);
                    }else {
                        mobile_et.setError(getString(R.string.invalid_mobile));
                    }

                }else if(viewValue==1){
                    countDownTimer.cancel();
                    timer_ll.setVisibility(View.GONE);
                    resend_btn.setVisibility(View.GONE);
                    otp=otp_et.getText().toString().trim();
                    if(otp.length()==6){
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("userId",mobileNumber);
                        jsonObject.addProperty("OTP",otp);
                        jsonObject.addProperty("cIPAddress",getIpAddress());
                        showLoader();
                        webApiCalls.verifyOtp(this::onOtpVerifyResponse,jsonObject);
                    }else {
                        otp_et.setError(getString(R.string.invalid_otp));
                    }
                }else if(viewValue==2){

                    password=password_et.getText().toString();
                    String confirm=confirm_et.getText().toString();
                    if(password.length()<6){
                        password_et.setError(getString(R.string.password_should_greater_6_char));
                    }else if(!password.equals(confirm)){
                        toast(getString(R.string.password_not_matched));
                    }else {
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("UserId",mobileNumber);
                        jsonObject.addProperty("NewPassword",password);
                        jsonObject.addProperty("CIPAddress",getIpAddress());
                        showLoader();
                        webApiCalls.updatePassword(this::onUpdatePasswordResponse,jsonObject);
                    }

                }

                break;
        }
    }


    void handleView() {
        if (viewValue == 0) {
            mobile_et.setVisibility(View.VISIBLE);
            otp_et.setVisibility(View.GONE);
            til_pass.setVisibility(View.GONE);
            til_confirm.setVisibility(View.GONE);
            verify_ll.setVisibility(View.GONE);
            timer_ll.setVisibility(View.GONE);
            resend_btn.setVisibility(View.GONE);
            level_tv.setText(R.string.associated_mobile);
            title_tv.setText(R.string.forgot_password_1);
            message_tv.setText(R.string.enter_associated_mobile);
            stopCountDownTimer();
        } else if (viewValue == 1) {
            mobile_et.setVisibility(View.GONE);
            til_pass.setVisibility(View.GONE);
            til_confirm.setVisibility(View.GONE);
            verify_ll.setVisibility(View.GONE);
            resend_btn.setVisibility(View.GONE);
            otp_et.setVisibility(View.VISIBLE);
            timer_ll.setVisibility(View.VISIBLE);

            level_tv.setText("OTP");
            title_tv.setText(R.string.otp_verification);
            send_btn.setText(R.string.verify);
            message_tv.setText(getString(R.string.enter_6_digit_otp)+mobileNumber);
            startCountDownTimer();

        } else if (viewValue == 2) {
            mobile_et.setVisibility(View.GONE);
            otp_et.setVisibility(View.GONE);
            til_pass.setVisibility(View.VISIBLE);
            til_confirm.setVisibility(View.VISIBLE);
            verify_ll.setVisibility(View.VISIBLE);
            timer_ll.setVisibility(View.GONE);
            resend_btn.setVisibility(View.GONE);
            level_tv.setText(R.string.new_password);
            title_tv.setText(R.string.create_password);
            message_tv.setText(R.string.different_password);
            send_btn.setText(R.string.submit);

        }
    }


    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                second_tv.setText((millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                timer_ll.setVisibility(View.GONE);
                resend_btn.setVisibility(View.VISIBLE);
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


    @Override
    public void onOtpResponse(OtpResponse otpResponse) {

        hideLoader();
        try {
            if(otpResponse.isStatus()){
                viewValue = viewValue + 1;
                handleView();
            }
            toast(otpResponse.getMessage());
        }catch (Exception e){

        }
    }

    @Override
    public void onUpdatePasswordResponse(OtpResponse otpResponse) {
        hideLoader();
        try {
            toast(otpResponse.getMessage());
            if(otpResponse.isStatus()){
                viewValue = viewValue + 1;
                handleView();
                finish();
            }

        }catch (Exception e){

        }
    }

    @Override
    public void onOtpVerifyResponse(OtpResponse otpResponse) {
        hideLoader();
        try {
            if(otpResponse.isStatus()){
                viewValue = viewValue + 1;
                handleView();
            }
            toast(otpResponse.getMessage());
        }catch (Exception e){

        }
    }
}
