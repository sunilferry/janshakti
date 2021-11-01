package apps.janshakti.model.login_model;

import com.google.gson.annotations.SerializedName;

public class ResultItem{

	@SerializedName("organizationId")
	private String organizationId;

	@SerializedName("latLongId")
	private String latLongId;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("longitude")
	private String longitude;

	public String getOrganizationId(){
		return organizationId;
	}

	public String getLatLongId(){
		return latLongId;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getLongitude(){
		return longitude;
	}
}