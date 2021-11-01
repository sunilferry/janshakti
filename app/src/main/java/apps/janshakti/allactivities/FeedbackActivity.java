package apps.janshakti.allactivities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import java.io.IOException;

import apps.janshakti.BuildConfig;
import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;
import apps.janshakti.callbacks.FeedbackCallback;
import apps.janshakti.model.OtpResponse;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener, FeedbackCallback {

    public static final int PICK_IMAGE = 1;
    ImageView image_iv, add_iv, back_iv;
    TextView app_tv, location_tv, suggestion_tv, other_tv;
    EditText message_et;
    Button submit_btn;
    String type = "App Problem", message = "", imageBase64 = "";
    Bitmap bitmap;
    ProgressBar progress_bar;
    private static final String TAG = "FeedbackActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        image_iv = findViewById(R.id.image_iv);
        app_tv = findViewById(R.id.app_tv);
        location_tv = findViewById(R.id.location_tv);
        suggestion_tv = findViewById(R.id.suggestion_tv);
        other_tv = findViewById(R.id.other_tv);
        submit_btn = findViewById(R.id.submit_btn);
        message_et = findViewById(R.id.message_et);
        add_iv = findViewById(R.id.add_iv);
        progress_bar = findViewById(R.id.progress_bar);
        back_iv = findViewById(R.id.back_iv);

        back_iv.setOnClickListener(this);
        image_iv.setOnClickListener(this);
        app_tv.setOnClickListener(this);
        location_tv.setOnClickListener(this);
        suggestion_tv.setOnClickListener(this);
        other_tv.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            Log.d(TAG, "onActivityResult: ");
            progress_bar.setVisibility(View.VISIBLE);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Uri uri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageBase64 = base64(bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                add_iv.setVisibility(View.GONE);
                                progress_bar.setVisibility(View.GONE);
                                image_iv.setImageURI(uri);
                            }
                        });
                        Log.d(TAG, "onActivityResult: " + imageBase64);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_iv:
                if (ContextCompat.checkSelfPermission(FeedbackActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    ActivityCompat.requestPermissions(FeedbackActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }

                break;
            case R.id.app_tv:
                app_tv.setBackgroundResource(R.drawable.solid_light_orang_4);
                app_tv.setTextColor(Color.BLACK);

                location_tv.setBackgroundResource(R.drawable.gray_border_5);
                location_tv.setTextColor(Color.WHITE);

                suggestion_tv.setBackgroundResource(R.drawable.gray_border_5);
                suggestion_tv.setTextColor(Color.WHITE);

                other_tv.setBackgroundResource(R.drawable.gray_border_5);
                other_tv.setTextColor(Color.WHITE);
                break;

            case R.id.location_tv:
                location_tv.setBackgroundResource(R.drawable.solid_light_orang_4);
                location_tv.setTextColor(Color.BLACK);

                app_tv.setBackgroundResource(R.drawable.gray_border_5);
                app_tv.setTextColor(Color.WHITE);

                suggestion_tv.setBackgroundResource(R.drawable.gray_border_5);
                suggestion_tv.setTextColor(Color.WHITE);

                other_tv.setBackgroundResource(R.drawable.gray_border_5);
                other_tv.setTextColor(Color.WHITE);

                break;

            case R.id.suggestion_tv:
                suggestion_tv.setBackgroundResource(R.drawable.solid_light_orang_4);
                suggestion_tv.setTextColor(Color.BLACK);

                app_tv.setBackgroundResource(R.drawable.gray_border_5);
                app_tv.setTextColor(Color.WHITE);

                location_tv.setBackgroundResource(R.drawable.gray_border_5);
                location_tv.setTextColor(Color.WHITE);

                other_tv.setBackgroundResource(R.drawable.gray_border_5);
                other_tv.setTextColor(Color.WHITE);
                break;

            case R.id.other_tv:
                other_tv.setBackgroundResource(R.drawable.solid_light_orang_4);
                other_tv.setTextColor(Color.BLACK);

                app_tv.setBackgroundResource(R.drawable.gray_border_5);
                app_tv.setTextColor(Color.WHITE);

                location_tv.setBackgroundResource(R.drawable.gray_border_5);
                location_tv.setTextColor(Color.WHITE);

                suggestion_tv.setBackgroundResource(R.drawable.gray_border_5);
                suggestion_tv.setTextColor(Color.WHITE);
                break;
            case R.id.submit_btn:
                message = message_et.getText().toString();
                if (message.isEmpty()) {
                    message_et.setError("Enter your message");
                } else {
                    showLoader();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("FeedbackSubject", type);
                    jsonObject.addProperty("FeedbackDescription", message);
                    jsonObject.addProperty("ImagePath", imageBase64);
                    jsonObject.addProperty("DeviceName", Build.MANUFACTURER + " " + Build.MODEL);
                    jsonObject.addProperty("DeviceVersion", String.valueOf(Build.VERSION.SDK_INT));
                    jsonObject.addProperty("AppVersion", BuildConfig.VERSION_NAME);
                    jsonObject.addProperty("CIPAddress", getIpAddress());
                    Log.d(TAG, "onClick: " + appSession.getAccessToken());
                    Log.d(TAG, "onClick: " + jsonObject);
                    webApiCalls.submitFeedback(this::onFeedbackResponse, appSession.getAccessToken(), jsonObject);

                }

                break;
            case R.id.back_iv:
                finish();
                break;


        }
    }

    @Override
    public void onFeedbackResponse(OtpResponse otpResponse) {
        try {
            hideLoader();
            toast(otpResponse.getMessage());
            bitmap = null;
            imageBase64 = "";
            image_iv.setImageDrawable(null);
            add_iv.setVisibility(View.VISIBLE);
            message_et.setText("");
        } catch (Exception e) {

        }
    }
}