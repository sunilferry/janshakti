package apps.janshakti.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apps.janshakti.R;
import apps.janshakti.model.attendance_list_model.DataItem;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.Holder> {
    private List<DataItem> list;

    public AttendanceAdapter(List<DataItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attendance_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.date_tv.setText(list.get(holder.getAdapterPosition()).getAttendanceDate());
        holder.type_tv.setText(list.get(holder.getAdapterPosition()).getAttendanceType());
        if(list.get(holder.getAdapterPosition()).getAttendanceType().equals("In")){
            holder.card.setCardBackgroundColor(Color.parseColor("#F7FCF7"));
        }else {
            holder.card.setCardBackgroundColor(Color.parseColor("#FCF6F8"));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView type_tv, date_tv;
        CardView card;


        public Holder(@NonNull View itemView) {
            super(itemView);
            date_tv = itemView.findViewById(R.id.date_tv);
            type_tv = itemView.findViewById(R.id.type_tv);
            card = itemView.findViewById(R.id.card);
        }
    }

}