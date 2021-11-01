package apps.janshakti.model.salary_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataItem implements Serializable {

	@SerializedName("wagesId")
	private String wagesId;

	@SerializedName("pasentDays")
	private String pasentDays;

	@SerializedName("year")
	private String year;

	@SerializedName("paymentMode")
	private String paymentMode;

	@SerializedName("esiAmount")
	private String esiAmount;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("employeeId")
	private String employeeId;

	@SerializedName("amountPaidtoOutsource")
	private String amountPaidtoOutsource;

	@SerializedName("epfAmount")
	private String epfAmount;

	@SerializedName("miss")
	private String miss;

	@SerializedName("esiChallanNo")
	private String esiChallanNo;

	@SerializedName("esiDate")
	private String esiDate;

	@SerializedName("month")
	private String month;

	@SerializedName("basicWages")
	private String basicWages;

	@SerializedName("agreementNo")
	private String agreementNo;

	@SerializedName("name")
	private String name;

	@SerializedName("epfDate")
	private String epfDate;

	@SerializedName("epftrrnNo")
	private String epftrrnNo;

	@SerializedName("category")
	private String category;

	@SerializedName("paymentDate")
	private String paymentDate;

	public String getWagesId(){
		return wagesId;
	}

	public String getPasentDays(){
		return pasentDays;
	}

	public String getYear(){
		return year;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public String getEsiAmount(){
		return esiAmount;
	}

	public String getMobile(){
		return mobile;
	}

	public String getEmployeeId(){
		return employeeId;
	}

	public String getAmountPaidtoOutsource(){
		return amountPaidtoOutsource;
	}

	public String getEpfAmount(){
		return epfAmount;
	}

	public String getMiss(){
		return miss;
	}

	public String getEsiChallanNo(){
		return esiChallanNo;
	}

	public String getEsiDate(){
		return esiDate;
	}

	public String getMonth(){
		return month;
	}

	public String getBasicWages(){
		return basicWages;
	}

	public String getAgreementNo(){
		return agreementNo;
	}

	public String getName(){
		return name;
	}

	public String getEpfDate(){
		return epfDate;
	}

	public String getEpftrrnNo(){
		return epftrrnNo;
	}

	public String getCategory(){
		return category;
	}

	public String getPaymentDate(){
		return paymentDate;
	}
}