package apps.janshakti.networking;

import com.google.gson.JsonObject;

import apps.janshakti.model.OtpResponse;
import apps.janshakti.model.VerificationResponse;
import apps.janshakti.model.attendance_list_model.AttendanceListResponse;
import apps.janshakti.model.login_model.LoginResponse;
import apps.janshakti.model.profile_model.ProfileResponse;
import apps.janshakti.model.punch_model.PunchResponse;
import apps.janshakti.model.salary_model.SalaryResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("userapi/Authenticate")
    Call<LoginResponse> login(@Body JsonObject jsonObject);

    @Headers({
            "Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: 490743c6f3ba498fac72bd000b044e8a"
    })
    @POST("verify")
    Call<VerificationResponse> verify(@Body JsonObject jsonObject);


    @POST("AttendanceApi/AttendancePunch")
    Call<PunchResponse> punch(@Body JsonObject jsonObject, @Header("Authorization") String token);

    @GET("AttendanceApi/GetProfile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

    @POST("AttendanceApi/GetAttendanceList")
    Call<AttendanceListResponse> getAttendanceList(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @GET("AttendanceApi/GetWagesList")
    Call<SalaryResponse> getSalaryList(@Header("Authorization") String token);


    @POST("Userapi/forgotPassword")
    Call<OtpResponse> sendOtp(@Body JsonObject jsonObject);

    @POST("Userapi/UpdatePassword")
    Call<OtpResponse> updatePassword(@Body JsonObject jsonObject);

    @POST("Userapi/OTPVerify")
    Call<OtpResponse> verifyOtp(@Body JsonObject jsonObject);

    @POST("AttendanceApi/ChangePassword")
    Call<OtpResponse> changePassword(@Header("Authorization") String token,@Body JsonObject jsonObject);

    @POST("AttendanceApi/Feedback")
    Call<OtpResponse> submitFeedback(@Header("Authorization") String token,@Body JsonObject jsonObject);


}
