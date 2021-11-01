package apps.janshakti.callbacks;

import apps.janshakti.model.attendance_list_model.AttendanceListResponse;

public interface AttendanceListCallback {
    void onAttendanceListResponse(AttendanceListResponse attendanceListResponse);
    void onAttendanceListFailed();
}
