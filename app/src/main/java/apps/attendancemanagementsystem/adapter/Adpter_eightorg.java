package apps.attendancemanagementsystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apps.attendancemanagementsystem.R;
import apps.attendancemanagementsystem.model.PojoOrgEight;

public class Adpter_eightorg extends RecyclerView.Adapter<Adpter_eightorg.Holder> {
    private List<PojoOrgEight> stringList;
    private Context context;
    private LayoutInflater layoutInflater;
    public EditOrgEight editOrgEight;

    public Adpter_eightorg(List<PojoOrgEight> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(layoutInflater.inflate(R.layout.growthwiseadapter, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (stringList.get(position).getNameofOffice().equalsIgnoreCase("") ||
                stringList.get(position).getNameofOffice().equalsIgnoreCase("null")) {
            holder.name_officer_tv.setText("--NA--");
        } else {
            holder.name_officer_tv.setText(stringList.get(position).getNameofOffice());
        }

        if (stringList.get(position).getAttendanceDate().equalsIgnoreCase("") ||
                stringList.get(position).getAttendanceDate().equalsIgnoreCase("null")) {
            holder.attendancedate_tv.setText("--NA--");
        } else {
            holder.attendancedate_tv.setText(stringList.get(position).getAttendanceDate());
        }


        if (stringList.get(position).getAttin().equalsIgnoreCase("") ||
                stringList.get(position).getAttin().equalsIgnoreCase("null")) {
            holder.tv_attin.setText("--NA--");
        } else {
            holder.tv_attin.setText(stringList.get(position).getAttin());
        }

        if (stringList.get(position).getAttOut().equalsIgnoreCase("") ||
                stringList.get(position).getAttOut().equalsIgnoreCase("null")) {
            holder.tv_attOut.setText("--NA--");
        } else {
            holder.tv_attOut.setText(stringList.get(position).getAttOut());
        }





    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView name_officer_tv, attendancedate_tv, tv_attin, tv_attOut;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.name_officer_tv = itemView.findViewById(R.id.name_officer_tv);
            this.attendancedate_tv = itemView.findViewById(R.id.attendancedate_tv);
            this.tv_attin = itemView.findViewById(R.id.tv_attin);
            this.tv_attOut = itemView.findViewById(R.id.tv_attOut);
        }
    }

    public interface EditOrgEight {
        void dataDelete(String position);
    }
}