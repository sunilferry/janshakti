package apps.janshakti.callbacks;

import apps.janshakti.model.OtpResponse;

public interface OtpCallback {
    void onOtpResponse(OtpResponse otpResponse);
}
