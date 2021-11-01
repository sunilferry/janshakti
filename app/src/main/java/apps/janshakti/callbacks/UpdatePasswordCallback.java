package apps.janshakti.callbacks;

import apps.janshakti.model.OtpResponse;

public interface UpdatePasswordCallback {
    void onUpdatePasswordResponse(OtpResponse otpResponse);
}
