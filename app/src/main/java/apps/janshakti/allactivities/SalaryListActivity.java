package apps.janshakti.allactivities;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;
import apps.janshakti.adapter.SalaryListAdapter;
import apps.janshakti.callbacks.SalaryCallback;
import apps.janshakti.callbacks.TimeOutCallback;
import apps.janshakti.model.salary_model.DataItem;
import apps.janshakti.model.salary_model.SalaryResponse;

public class SalaryListActivity extends BaseActivity implements SalaryCallback , TimeOutCallback {
    TextView name_tv;
    ImageView back_iv;
    RecyclerView recyclerView;
    LinearLayout no_data_ll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_list);
        initView();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showLoader();
        webApiCalls.getSalaryList(this::onSalaryResponse,this::onTimeOut ,appSession.getAccessToken());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        no_data_ll = findViewById(R.id.no_data_ll);
        name_tv = findViewById(R.id.name_tv);
        back_iv = findViewById(R.id.back_iv);
        name_tv.setText("Hey " + appSession.getUsername() + ",");
    }

    @Override
    public void onSalaryResponse(SalaryResponse salaryResponse) {
        try {
            hideLoader();
            if (salaryResponse.isStatus()) {

                if (salaryResponse.getData().size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    no_data_ll.setVisibility(View.GONE);
                    SalaryListAdapter salaryListAdapter = new SalaryListAdapter(salaryResponse.getData(), new SalaryListAdapter.ItemClick() {
                        @Override
                        public void onClick(DataItem dataItem) {
                          /*  Intent intent=new Intent(SalaryListActivity.this,SalaryDetailActivity.class);
                            intent.putExtra("data",dataItem);
                            startActivity(intent);*/

                        }
                    });
                    recyclerView.setAdapter(salaryListAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    no_data_ll.setVisibility(View.VISIBLE);
                }

            } else {
                recyclerView.setVisibility(View.GONE);
                no_data_ll.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            recyclerView.setVisibility(View.GONE);
            no_data_ll.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onTimeOut(String from) {
        hideLoader();
    }
}