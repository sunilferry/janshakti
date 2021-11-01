package apps.janshakti.callbacks;


import apps.janshakti.model.VerificationResponse;

public interface VerificationCallback {
    void onResponse(VerificationResponse verificationResponse);
    void onVerifyFailed();
}
