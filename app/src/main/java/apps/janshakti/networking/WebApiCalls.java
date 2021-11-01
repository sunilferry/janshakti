package apps.janshakti.networking;


import android.util.Log;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import apps.janshakti.callbacks.AttendanceListCallback;
import apps.janshakti.callbacks.FeedbackCallback;
import apps.janshakti.callbacks.LoginCallback;
import apps.janshakti.callbacks.OtpCallback;
import apps.janshakti.callbacks.ProfileResponseCallback;
import apps.janshakti.callbacks.PunchCallback;
import apps.janshakti.callbacks.SalaryCallback;
import apps.janshakti.callbacks.TimeOutCallback;
import apps.janshakti.callbacks.UpdatePasswordCallback;
import apps.janshakti.callbacks.VerificationCallback;
import apps.janshakti.callbacks.VerifyOtpCallback;
import apps.janshakti.model.OtpResponse;
import apps.janshakti.model.VerificationResponse;
import apps.janshakti.model.attendance_list_model.AttendanceListResponse;
import apps.janshakti.model.login_model.LoginResponse;
import apps.janshakti.model.profile_model.ProfileResponse;
import apps.janshakti.model.punch_model.PunchResponse;
import apps.janshakti.model.salary_model.SalaryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebApiCalls {
    private static final String TAG = "WebApiCalls";

    public void login(LoginCallback loginCallback, JsonObject jsonObject) {
        Call<LoginResponse> call = RetrofitClient.getInstance().getMyApi().login(jsonObject);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {

                try {
                    loginCallback.onLoginResponse(response.body());
                } catch (Exception e) {
                    loginCallback.onLoginFailed();
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
                loginCallback.onLoginFailed();
            }
        });
    }

    public void verify(final VerificationCallback verificationCallback, TimeOutCallback timeOutCallback, JsonObject jsonObject) {
        Log.d(TAG, "verify: " + jsonObject);
        Call<VerificationResponse> call = RetrofitClient.getClient().verify(jsonObject);
        call.enqueue(new Callback<VerificationResponse>() {
            @Override
            public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                try {
                    Log.d(TAG, "onResponse: " + response.body());
                    if (response.body() == null) {
                        verificationCallback.onVerifyFailed();
                    }
                    if (response.isSuccessful()) {
                        verificationCallback.onResponse(response.body());
                        Log.d(TAG, "onResponse: " + response.body().isIsIdentical());
                    }

                } catch (Exception e) {
                    verificationCallback.onVerifyFailed();
                    Log.d(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<VerificationResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
                if (Objects.equals(t.getLocalizedMessage(), "timeout")) {
                    timeOutCallback.onTimeOut("verify");
                } else {
                    verificationCallback.onVerifyFailed();
                }

            }
        });
    }

    public void punchAttendance(PunchCallback punchCallback, TimeOutCallback timeOutCallback, JsonObject jsonObject, String token) {
        Log.d(TAG, "punchAttendance: "+jsonObject);
        Call<PunchResponse> call = RetrofitClient.getInstance().getMyApi().punch(jsonObject, "Bearer " + token);
        call.enqueue(new Callback<PunchResponse>() {
            @Override
            public void onResponse(@NotNull Call<PunchResponse> call, @NotNull Response<PunchResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        punchCallback.onPunchResponse(response.body());
                    } else {
                        timeOutCallback.onTimeOut("failed");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    timeOutCallback.onTimeOut("failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<PunchResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + t.getLocalizedMessage());
                if (Objects.equals(t.getLocalizedMessage(), "timeout")) {
                    timeOutCallback.onTimeOut("punch");
                } else {
                    timeOutCallback.onTimeOut("failed");
                }

            }
        });
    }

    public void getProfile(ProfileResponseCallback profileResponseCallback, String token) {
        Call<ProfileResponse> call = RetrofitClient.getInstance().getMyApi().getProfile("Bearer " + token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull Response<ProfileResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        profileResponseCallback.onProfileResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                try {

                    Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());

                }catch (Exception e){


                }

            }
        });
    }

    public void getAttendanceList(AttendanceListCallback attendanceListCallback, JsonObject jsonObject, String token) {
        Call<AttendanceListResponse> call = RetrofitClient.getInstance().getMyApi().getAttendanceList("Bearer " + token, jsonObject);
        call.enqueue(new Callback<AttendanceListResponse>() {
            @Override
            public void onResponse(@NotNull Call<AttendanceListResponse> call, @NotNull Response<AttendanceListResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        attendanceListCallback.onAttendanceListResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    attendanceListCallback.onAttendanceListFailed();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AttendanceListResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
                attendanceListCallback.onAttendanceListFailed();
            }
        });
    }

    public void getSalaryList(SalaryCallback salaryCallback,TimeOutCallback timeOutCallback, String token) {
        Call<SalaryResponse> call = RetrofitClient.getInstance().getMyApi().getSalaryList("Bearer " + token);
        call.enqueue(new Callback<SalaryResponse>() {
            @Override
            public void onResponse(@NotNull Call<SalaryResponse> call, @NotNull Response<SalaryResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        salaryCallback.onSalaryResponse(response.body());
                    }
                } catch (Exception e) {
                    timeOutCallback.onTimeOut("");
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());

                }
            }

            @Override
            public void onFailure(@NotNull Call<SalaryResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
                timeOutCallback.onTimeOut("");

            }
        });
    }
    public void sendOtp(OtpCallback otpCallback,JsonObject jsonObject) {
        Call<OtpResponse> call = RetrofitClient.getInstance().getMyApi().sendOtp(jsonObject);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        otpCallback.onOtpResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
            }
        });
    }

    public void updatePassword(UpdatePasswordCallback updatePasswordCallback, JsonObject jsonObject) {
        Call<OtpResponse> call = RetrofitClient.getInstance().getMyApi().updatePassword(jsonObject);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        updatePasswordCallback.onUpdatePasswordResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
            }
        });
    }

    public void verifyOtp(VerifyOtpCallback verifyOtpCallback, JsonObject jsonObject) {
        Log.d(TAG, "verifyOtp: "+jsonObject);
        Call<OtpResponse> call = RetrofitClient.getInstance().getMyApi().verifyOtp(jsonObject);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        verifyOtpCallback.onOtpVerifyResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
            }
        });
    }

    public void changePassword(VerifyOtpCallback verifyOtpCallback, String token,JsonObject jsonObject) {
        Log.d(TAG, "verifyOtp: "+jsonObject);
        Call<OtpResponse> call = RetrofitClient.getInstance().getMyApi().changePassword("Bearer " + token,jsonObject);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        verifyOtpCallback.onOtpVerifyResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
            }
        });
    }

    public void submitFeedback(FeedbackCallback feedbackCallback, String token, JsonObject jsonObject) {
        Call<OtpResponse> call = RetrofitClient.getInstance().getMyApi().submitFeedback("Bearer " + token,jsonObject);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.body());
                        feedbackCallback.onFeedbackResponse(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().url() + " " + t.getLocalizedMessage());
            }
        });
    }

}
