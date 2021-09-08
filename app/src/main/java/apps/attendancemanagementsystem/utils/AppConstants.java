package apps.attendancemanagementsystem.utils;

import java.util.regex.Pattern;

public interface AppConstants {
    String serverurl2 = "https://pmsupfd.in/";
    String serverUrl1 = "https://pmsupfd.in/Service1.svc/";
    String serverUrl3 = "https://pmsupfd.in/api/NMS/";
    String getLogin = serverUrl1 + "Role";
    String uploadDemandLetter = serverUrl1 + "UploadDemandLetter";
    String sentPlantTransactionList = serverUrl1 + "SentPlantTransactionList/";
    String plantTransaction = serverUrl1 + "PlantTransaction";
    String getDemandSpecies = serverUrl1 + "GetDemandSpecies/";
    String getDemandLetterDetail = serverUrl1 + "GetDemandLetterDetail/";
    String getPartialLiftedPlantsDetail = serverUrl1 + "GetPartialLiftedPlantsDetail/";

    String GetGeoTaggedSitesLatLong = serverUrl3 + "GetGeoTaggedSitesLatLong";
    String GetOtherDepartmentPlantationsites = serverUrl3 + "GetOtherDepartmentPlantationsites";
    String GetOtherDepartmentPlantationSitesInDetail = serverUrl3 + "GetOtherDepartmentPlantationSitesInDetail";
    String InsertOtherDepartmentGeoTaggedSites = serverUrl3 + "InsertOtherDepartmentGeoTaggedSites";

    String DeleteOtherDepartmentGeoTaggedSites = serverUrl3 + "DeleteGeoTaggedSites";

    String IMAGE_DIRECTORY = "/DCIM/PICTURES";
    String IMAGE_DIRECTORY_CROP = "/DCIM/CROP_PICTURES";
    String VIDEO_DIRECTORY = "/DCIM/VIDEOS";

    //    * Constant for Intent calling     */
    int ACTIVITY_RESULT = 1001, ACTIVITY_FINISH = 1002, GALLERY = 111, CAMERA = 112, CROP = 113;

    Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^([a-zA-Z0-9._-]+)@{1}(([a-zA-Z0-9_-]{1,67})|([a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{1,67}))\\.(([a-zA-Z0-9]{2,6})(\\.[a-zA-Z0-9]{2,6})?)$");
    Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^[0-9]{8,14}$");
    Pattern USER_NAME_PATTERN = Pattern.compile("^([a-zA-Z0-9._-]){6,20}$");
    Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$");


    //    new api's on 8thJune2020
    String GetCountDashboard = serverUrl3 + "GetCountDashboard";
}