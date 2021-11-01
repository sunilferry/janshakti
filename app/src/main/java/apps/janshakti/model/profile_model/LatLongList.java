package apps.janshakti.model.profile_model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LatLongList{

	@SerializedName("result")
	private List<ResultItem> result;

	@SerializedName("isFaulted")
	private boolean isFaulted;

	@SerializedName("isCanceled")
	private boolean isCanceled;

	@SerializedName("creationOptions")
	private int creationOptions;

	@SerializedName("isCompletedSuccessfully")
	private boolean isCompletedSuccessfully;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private int status;

	@SerializedName("isCompleted")
	private boolean isCompleted;

	public List<ResultItem> getResult(){
		return result;
	}

	public boolean isIsFaulted(){
		return isFaulted;
	}

	public boolean isIsCanceled(){
		return isCanceled;
	}

	public int getCreationOptions(){
		return creationOptions;
	}

	public boolean isIsCompletedSuccessfully(){
		return isCompletedSuccessfully;
	}

	public int getId(){
		return id;
	}

	public int getStatus(){
		return status;
	}

	public boolean isIsCompleted(){
		return isCompleted;
	}
}