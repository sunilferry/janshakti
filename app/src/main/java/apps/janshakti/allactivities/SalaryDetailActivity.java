package apps.janshakti.allactivities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;

import apps.janshakti.R;
import apps.janshakti.activity.BaseActivity;
import apps.janshakti.model.salary_model.DataItem;

public class SalaryDetailActivity extends BaseActivity implements View.OnClickListener {

    ImageView back_iv;
    TextView gross_tv,earning_tv,deduction_tv,pf_tv,esi_tv,basic_tv,total_tv,month_tv;
    DataItem dataItem;
    ProgressBar progress_bar;

    String []MONTHS={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_detail);
        Bundle bundle=getIntent().getExtras();
        dataItem= (DataItem) bundle.getSerializable("data");
        initView();
    }

    void initView() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("INR"));

        back_iv = findViewById(R.id.back_iv);
        gross_tv = findViewById(R.id.gross_tv);
        earning_tv = findViewById(R.id.earning_tv);
        deduction_tv = findViewById(R.id.deduction_tv);
        pf_tv = findViewById(R.id.pf_tv);
        esi_tv = findViewById(R.id.esi_tv);
        basic_tv = findViewById(R.id.basic_tv);
        total_tv = findViewById(R.id.total_tv);
        progress_bar = findViewById(R.id.progress_bar);
        month_tv = findViewById(R.id.month_tv);

        month_tv.setText(MONTHS[Integer.parseInt(dataItem.getMonth())-1]);

        gross_tv.setText(format.format(Double.parseDouble(dataItem.getBasicWages())));
        earning_tv.setText(format.format(Double.parseDouble(dataItem.getAmountPaidtoOutsource())));
        double deduction=Double.parseDouble(dataItem.getEpfAmount())+Double.parseDouble(dataItem.getEsiAmount());
        deduction_tv.setText(format.format(deduction));
        pf_tv.setText(format.format(Double.parseDouble(dataItem.getEpfAmount())));
        esi_tv.setText(format.format(Double.parseDouble(dataItem.getEsiAmount())));
        double basic=Double.parseDouble(dataItem.getBasicWages())-deduction;
        basic_tv.setText(format.format(basic));
        total_tv.setText(format.format(Double.parseDouble(dataItem.getBasicWages())));
        int max= Integer.parseInt(dataItem.getBasicWages());
        progress_bar.setMax(max);
        double earning=Double.parseDouble(dataItem.getAmountPaidtoOutsource());

        int progress= (int)earning;
        progress_bar.setProgress(progress);


        back_iv.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }
}