package apps.janshakti.model.login_model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("response")
	private Response response;

	@SerializedName("latLongList")
	private LatLongList latLongList;

	public Response getResponse(){
		return response;
	}

	public LatLongList getLatLongList(){
		return latLongList;
	}
}