package apps.janshakti.callbacks;

import apps.janshakti.model.OtpResponse;
import apps.janshakti.model.VerificationResponse;

public interface FeedbackCallback {
    void onFeedbackResponse(OtpResponse otpResponse);
}
