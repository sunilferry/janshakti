package apps.janshakti.callbacks;

import apps.janshakti.model.salary_model.SalaryResponse;

public interface SalaryCallback {
    void onSalaryResponse(SalaryResponse salaryResponse);
}
