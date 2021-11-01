package apps.janshakti.model.profile_model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("obj")
	private Obj obj;

	@SerializedName("latLongList")
	private LatLongList latLongList;

	public Obj getObj(){
		return obj;
	}

	public LatLongList getLatLongList(){
		return latLongList;
	}
}