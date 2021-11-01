package apps.janshakti.model.profile_model;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("lastLoginDateTime")
	private String lastLoginDateTime;

	@SerializedName("role")
	private String role;

	@SerializedName("roleId")
	private String roleId;

	@SerializedName("employeeId")
	private String employeeId;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("userName")
	private String userName;

	@SerializedName("userId")
	private String userId;

	@SerializedName("lastLoginIPAddress")
	private String lastLoginIPAddress;

	@SerializedName("officeId")
	private String officeId;

	@SerializedName("currentIPAddress")
	private String currentIPAddress;

	@SerializedName("jwtToken")
	private String jwtToken;

	@SerializedName("id")
	private String id;

	@SerializedName("currentLoginDateTime")
	private String currentLoginDateTime;

	@SerializedName("email")
	private String email;

	@SerializedName("officeType")
	private String officeType;

	@SerializedName("refreshToken")
	private String refreshToken;

	public String getLastLoginDateTime(){
		return lastLoginDateTime;
	}

	public String getRole(){
		return role;
	}

	public String getRoleId(){
		return roleId;
	}

	public String getEmployeeId(){
		return employeeId;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public String getUserName(){
		return userName;
	}

	public String getUserId(){
		return userId;
	}

	public String getLastLoginIPAddress(){
		return lastLoginIPAddress;
	}

	public String getOfficeId(){
		return officeId;
	}

	public String getCurrentIPAddress(){
		return currentIPAddress;
	}

	public String getJwtToken(){
		return jwtToken;
	}

	public String getId(){
		return id;
	}

	public String getCurrentLoginDateTime(){
		return currentLoginDateTime;
	}

	public String getEmail(){
		return email;
	}

	public String getOfficeType(){
		return officeType;
	}

	public String getRefreshToken(){
		return refreshToken;
	}
}