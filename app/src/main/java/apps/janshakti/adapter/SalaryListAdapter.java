package apps.janshakti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import apps.janshakti.R;
import apps.janshakti.model.salary_model.DataItem;

public class SalaryListAdapter extends RecyclerView.Adapter<SalaryListAdapter.Holder> {
    String []MONTHS={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private final List<DataItem> list;
    ItemClick itemClick;

    NumberFormat format = NumberFormat.getCurrencyInstance();


    public SalaryListAdapter(List<DataItem> list,ItemClick itemClick) {
        this.list = list;
        this.itemClick = itemClick;
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("INR"));
    }
    public interface ItemClick{
        void onClick(DataItem dataItem);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.salary_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        String formatedDate=MONTHS[Integer.parseInt(list.get(position).getMonth())-1]+" "+ list.get(position).getYear();

        holder.date_tv.setText(formatedDate);
        holder.amount_tv.setText(format.format(Double.parseDouble(list.get(position).getAmountPaidtoOutsource())));
        holder.epf_amount_tv.setText(format.format(Double.parseDouble(list.get(position).getEpfAmount())));
        holder.esic_amount_tv.setText(format.format(Double.parseDouble(list.get(position).getEsiAmount())));
        holder.epf_number_tv.setText(list.get(position).getEpftrrnNo());
        holder.esic_number_tv.setText(list.get(position).getEsiChallanNo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView amount_tv,epf_amount_tv,epf_number_tv,esic_amount_tv,esic_number_tv,date_tv,day_tv;
        public Holder(@NonNull View itemView) {
            super(itemView);
            amount_tv=itemView.findViewById(R.id.amount_tv);
            epf_amount_tv=itemView.findViewById(R.id.epf_amount_tv);
            esic_amount_tv=itemView.findViewById(R.id.esic_amount_tv);
            epf_number_tv=itemView.findViewById(R.id.epf_number_tv);
            esic_number_tv=itemView.findViewById(R.id.esic_number_tv);
            date_tv=itemView.findViewById(R.id.date_tv);
            day_tv=itemView.findViewById(R.id.day_tv);
        }
    }

}