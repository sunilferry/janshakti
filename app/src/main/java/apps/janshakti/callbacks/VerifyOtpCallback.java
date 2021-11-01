package apps.janshakti.callbacks;

import apps.janshakti.model.OtpResponse;

public interface VerifyOtpCallback {
    void onOtpVerifyResponse(OtpResponse otpResponse);
}
