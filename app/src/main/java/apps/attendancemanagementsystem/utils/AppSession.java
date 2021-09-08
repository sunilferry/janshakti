package apps.attendancemanagementsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppSession {
    //Declaration of variables
    private static final String SESSION_NAME = "apps.nms";
    private static final String APP_DEFAULT_LANGUAGE = "en";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    //Constructor
    public AppSession(Context context) {
        mSharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        prefsEditor = mSharedPreferences.edit();
    }

    public boolean isLogin() {
        return mSharedPreferences.getBoolean("Login", false);
    }

    public void setLogin(boolean Login) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putBoolean("Login", Login);
        prefsEditor.commit();
    }

    public String getSubLocality() {
        return mSharedPreferences.getString("getSubLocality", "");
    }

    public void setSubLocality(String password) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getSubLocality", password);
        prefsEditor.commit();
    }

    public String getAccessToken() {
        return mSharedPreferences.getString("getAccessToken", "");
    }

    public void setAccessToken(String accessToken) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAccessToken", accessToken);
        prefsEditor.commit();
    }

    public String getHertiageDistrictId() {
        return mSharedPreferences.getString("getHertiageDistrictId", "");
    }

    public void setHertiageDistrictId(String hertiageDistrictId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getHertiageDistrictId", hertiageDistrictId);
        prefsEditor.commit();
    }

    public String getName() {
        return mSharedPreferences.getString("getName", "");
    }

    public void setName(String name) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getName", name);
        prefsEditor.commit();
    }

    public String getMobile() {
        return mSharedPreferences.getString("getMobile", "");
    }

    public void setMobile(String mobile) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getMobile", mobile);
        prefsEditor.commit();
    }

    //for nodistrict_nonurseryid...
    public String getNoDistrictNurseryMinValue() {
        return mSharedPreferences.getString("getNoDistrictNurseryMinValue", "");
    }

    public void setNoDistrictNurseryMinValue(String noDistrictNurseryMinValue) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getNoDistrictNurseryMinValue", noDistrictNurseryMinValue);
        prefsEditor.commit();
    }

    public String getNoDistrictNurseryMaxValue() {
        return mSharedPreferences.getString("getNoDistrictNurseryMaxValue", "");
    }

    public void setNoDistrictNurseryMaxValue(String noDistrictNurserymaxValue) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getNoDistrictNurseryMaxValue", noDistrictNurserymaxValue);
        prefsEditor.commit();
    }

    public Uri getImageUri() {
        String imageUri = mSharedPreferences.getString("getImageUri", "");
        if (imageUri == null || imageUri.equals(""))
            return null;
        return Uri.parse(imageUri);
    }

    public void setImageUri(Uri imageUri) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getImageUri", imageUri.toString());
        prefsEditor.commit();
    }

    public String getImagePath() {
        return mSharedPreferences.getString("getImagePath", "");
    }

    public void setImagePath(String imagePath) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getImagePath", imagePath);
        prefsEditor.commit();
    }

    public String getCropImagePath() {
        return mSharedPreferences.getString("getCropImagePath", "");
    }

    public void setCropImagePath(String cropImagePath) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getCropImagePath", cropImagePath);
        prefsEditor.commit();
    }

    //FCM
    public String getFCMToken() {
        return mSharedPreferences.getString("FCMToken", "");
    }

    public void setFCMToken(String FCMToken) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("FCMToken", FCMToken);
        prefsEditor.commit();
    }

    public String getGender() {
        return mSharedPreferences.getString("getGender", "");
    }

    public void setGender(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getGender", genderId);
        prefsEditor.commit();
    }

    public String getMainGender() {
        return mSharedPreferences.getString("getMainGender", "");
    }

    public void setMainGender(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getMainGender", genderId);
        prefsEditor.commit();
    }

    public String getAppointDate() {
        return mSharedPreferences.getString("getAppointDate", "");
    }

    public void setAppointDate(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAppointDate", genderId);
        prefsEditor.commit();
    }

    public String getAppointTime() {
        return mSharedPreferences.getString("getAppointTime", "");
    }

    public void setAppointTime(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAppointTime", genderId);
        prefsEditor.commit();
    }

    public String getAppointHome() {
        return mSharedPreferences.getString("getAppointHome", "");
    }

    public void setAppointHome(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAppointHome", genderId);
        prefsEditor.commit();
    }

    public String getAddressId() {
        return mSharedPreferences.getString("getAddressId", "");
    }

    public void setAddressId(String genderId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAddressId", genderId);
        prefsEditor.commit();
    }

//    public ArrayList<DistrictCodeModel> getDistrictList() {
//        Gson gson = new Gson();
//        String json = mSharedPreferences.getString("getDistrictList", "");
//        Type type = new TypeToken<List<DistrictCodeModel>>() {
//        }.getType();
//        ArrayList<DistrictCodeModel> listAdditionalService = new ArrayList<>();
//        listAdditionalService = gson.fromJson(json, type);
//        return listAdditionalService;
//    }
//
//    public void setDistrictList(ArrayList<DistrictCodeModel> listService) {
//        Gson gson = new Gson();
//        String json = gson.toJson(listService);
//        prefsEditor = mSharedPreferences.edit();
//        prefsEditor.putString("getDistrictList", json);
//        prefsEditor.apply();
//    }

    public String getDistrictId() {
        return mSharedPreferences.getString("getDistrictId", "");
    }

    public void setDistrictId(String districtId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getDistrictId", districtId);
        prefsEditor.commit();
    }


    public String getUsername() {
        return mSharedPreferences.getString("getUsername", "");
    }

    public void setUsername(String username) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getUsername", username);
        prefsEditor.commit();
    }

    public String getNameofMandi() {
        return mSharedPreferences.getString("getNameofMandi", "");
    }

    public void setNameofMandi(String nameofMandi) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getNameofMandi", nameofMandi);
        prefsEditor.commit();
    }


    public String getMandiUniqueId() {
        return mSharedPreferences.getString("getMandiUniqueId", "");
    }

    public void setMandiUniqueId(String mandiUniqueId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getMandiUniqueId", mandiUniqueId);
        prefsEditor.commit();
    }


    public String getAddressofMandi() {
        return mSharedPreferences.getString("getAddressofMandi", "");
    }

    public void setAddressofMandi(String addressofMandi) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getAddressofMandi", addressofMandi);
        prefsEditor.commit();
    }


    public String getEmployeeUserId() {
        return mSharedPreferences.getString("getEmployeeUserId", "");
    }

    public void setEmployeeUserId(String employeeUserId) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getEmployeeUserId", employeeUserId);
        prefsEditor.commit();
    }


    public String getIP_address() {
        return mSharedPreferences.getString("getIP_address", "");
    }

    public void setIP_address(String ip_address) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getIP_address", ip_address);
        prefsEditor.commit();
    }


    public String getMobileNumber() {
        return mSharedPreferences.getString("getMobileNumber", "");
    }

    public void setMobileNumber(String mobileNumber) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getMobileNumber", mobileNumber);
        prefsEditor.commit();
    }


    public String getEmailAddress() {
        return mSharedPreferences.getString("getEmailAddress", "");
    }

    public void setEmailAddress(String emailAddress) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getEmailAddress", emailAddress);
        prefsEditor.commit();
    }


    public String getFirebaseToekn() {
        return mSharedPreferences.getString("getFirebaseToekn", "");
    }

    public void setFirebaseToekn(String firebaseToekn) {
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("getFirebaseToekn", firebaseToekn);
        prefsEditor.commit();
    }
}
