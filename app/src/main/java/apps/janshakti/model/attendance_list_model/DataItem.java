package apps.janshakti.model.attendance_list_model;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("attendanceType")
	private String attendanceType;

	@SerializedName("employeeName")
	private String employeeName;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("attendanceDate")
	private String attendanceDate;

	@SerializedName("longitude")
	private String longitude;

	public String getAttendanceType(){
		return attendanceType;
	}

	public String getEmployeeName(){
		return employeeName;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public String getAttendanceDate(){
		return attendanceDate;
	}

	public String getLongitude(){
		return longitude;
	}
}