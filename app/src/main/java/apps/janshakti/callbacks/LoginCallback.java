package apps.janshakti.callbacks;

import apps.janshakti.model.login_model.LoginResponse;

public interface LoginCallback {
    void onLoginResponse(LoginResponse loginResponse);
    void onLoginFailed();
}
