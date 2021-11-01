package apps.janshakti.model;

import com.google.gson.annotations.SerializedName;

public class VerificationResponse {

	@SerializedName("confidence")
	private double confidence;

	@SerializedName("isIdentical")
	private boolean isIdentical;

	public double getConfidence(){
		return confidence;
	}

	public boolean isIsIdentical(){
		return isIdentical;
	}
}