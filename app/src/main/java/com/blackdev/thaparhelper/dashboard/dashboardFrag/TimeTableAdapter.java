package com.blackdev.thaparhelper.dashboard.dashboardFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.database.TimeTableData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TimeTableAdapter  extends RecyclerView.Adapter<TimeTableBaseViewHolder> {

    public static final int DAYS_TYPE = 101;
    public static final int CLASS_TYPE = 102;
    Context context;
    List<TimeTableData> list;

    public TimeTableAdapter(Context context, List<TimeTableData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TimeTableBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case DAYS_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_day_layout,parent,false);
                return new DayViewHolder(view);
            case CLASS_TYPE:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time_table,parent,false);
                return new ListItemViewHolder(view1);
        }


        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getClassType() == -1) {
            return DAYS_TYPE;
        } else {
            return CLASS_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableBaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DayViewHolder extends TimeTableBaseViewHolder {
        TextView dayTextView;
        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.row_day_text_view);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            String text = Constants.DAYLIST[list.get(position).getmDay()];
            dayTextView.setText(text);
        }
    }

    class ListItemViewHolder extends TimeTableBaseViewHolder {

        ImageView classImage;
        TextView classTime,className;
        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            classImage = itemView.findViewById(R.id.row_class_type_image);
            classTime = itemView.findViewById(R.id.row_class_time);
            className = itemView.findViewById(R.id.row_class_name);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            String text = Constants.TYPE_LIST[list.get(position).getClassType()]+" at "+list.get(position).getmHH()+":"+list.get(position).getmMM();
            className.setText(list.get(position).getmSubjectName());
            classTime.setText(text);
            switch (list.get(position).getClassType()+1) {
                case 1:
                    classImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_lecture));
                    break;
                case 2:
                    classImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_tutorial));
                    break;
                case 3:
                    classImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_lab));
                    break;
            }




        }
    }


}
