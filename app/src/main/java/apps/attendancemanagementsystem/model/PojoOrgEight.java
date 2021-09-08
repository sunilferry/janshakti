package apps.attendancemanagementsystem.model;

public class PojoOrgEight {
    String AttendanceDate;
    String NameofOffice;
    String Attin;
    String AttOut;

    public PojoOrgEight(String attendanceDate, String nameofOffice, String attin,String attOut) {
        this.AttendanceDate = attendanceDate;
        this.NameofOffice = nameofOffice;
        this.Attin = attin;
        this.AttOut = attOut;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public String getNameofOffice() {
        return NameofOffice;
    }

    public void setNameofOffice(String nameofOffice) {
        NameofOffice = nameofOffice;
    }

    public String getAttin() {
        return Attin;
    }

    public void setAttin(String attin) {
        Attin = attin;
    }

    public String getAttOut() {
        return AttOut;
    }

    public void setAttOut(String attOut) {
        AttOut = attOut;
    }
}
