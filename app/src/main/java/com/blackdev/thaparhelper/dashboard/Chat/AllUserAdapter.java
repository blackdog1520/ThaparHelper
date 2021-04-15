package com.blackdev.thaparhelper.dashboard.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.UserPersonalData;
import com.blackdev.thaparhelper.allutils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyHolder> {
    Context context;
    ArrayList<UserPersonalData> list;
    public AllUserAdapter(Context context,ArrayList<UserPersonalData> list){
        this.context = context;
        this.list = list;
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView username,batch;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.userProfileImageAllUsers);
            username = itemView.findViewById(R.id.userNameAllUser);
            batch = itemView.findViewById(R.id.userBatchAllUser);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users_list,parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final UserPersonalData data = list.get(position);
        if(!data.getProfileImageLink().isEmpty()) {
            Picasso.get().load(list.get(position).getProfileImageLink()).into(holder.profile);
        }
        holder.username.setText(data.getName());
        holder.batch.setText(data.getBatch());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserChatHolderActivity.class);
                intent.putExtra("HisUID",data.getUid());
                intent.putExtra("HisName",data.getName());
                intent.putExtra("HisDept",data.getBatch());
                intent.putExtra("HisProfile",data.getProfileImageLink());
                intent.putExtra("HisType", Constants.USER_ADMINISTRATION);
                context.startActivity(intent);
            }
        });
    }
    void assignList(ArrayList<UserPersonalData> newList) {
        list = newList;
        notifyDataSetChanged();
    }
    void addItem(UserPersonalData data) {
        list.add(data);
    }

    void clearList() {
        list.clear();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

