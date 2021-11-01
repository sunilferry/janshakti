package apps.janshakti.model;

public class PojoLatLong {
    String NameofOffice;
    Double Lat;
    Double Longittude;
    String officeId;


    public PojoLatLong(String nameofOffice, Double lat, Double longtitude,String officeId) {
        this.NameofOffice = nameofOffice;
        this.Lat = lat;
        this.Longittude = longtitude;
        this.officeId = officeId;

    }

    public String getNameofOffice() {
        return NameofOffice;
    }

    public void setNameofOffice(String nameofOffice) {
        NameofOffice = nameofOffice;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLongittude() {
        return Longittude;
    }

    public void setLongittude(Double longittude) {
        Longittude = longittude;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
