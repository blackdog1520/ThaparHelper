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
import com.blackdev.thaparhelper.dashboard.Chat.adapter.GroupChatAdapter;
import com.blackdev.thaparhelper.database.ChatData;
import com.blackdev.thaparhelper.database.RecentChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyHolder> {
    Context context;
    ArrayList<UserPersonalData> list;
    List<RecentChatData> list2;
    int type;
    public AllUserAdapter(Context context,ArrayList<UserPersonalData> list){
        this.context = context;
        this.list = list;
        type = 1;
    }

    public AllUserAdapter(Context context,List<RecentChatData> list){
        this.context = context;
        this.list2 =  list;
        type = 2;
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
        if(type == 1) {
            final UserPersonalData data = list.get(position);
            if (!data.getProfileImageLink().isEmpty()) {
                Picasso.get().load(list.get(position).getProfileImageLink()).into(holder.profile);
            }
            holder.username.setText(data.getName());
            holder.batch.setText(data.getBatch());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserChatHolderActivity.class);
                    intent.putExtra("HisUID", data.getUid());
                    intent.putExtra("HisName", data.getName());
                    intent.putExtra("HisDept", data.getBatch());
                    intent.putExtra("HisProfile", data.getProfileImageLink());
                    intent.putExtra("HisType", Constants.USER_ADMINISTRATION);
                    context.startActivity(intent);
                }
            });
        } else {
            final RecentChatData data = list2.get(position);
            if (!data.getImageLink().isEmpty()) {
                Picasso.get().load(list2.get(position).getImageLink()).into(holder.profile);
            }
            holder.username.setText(data.getName());
            holder.batch.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getType() == Constants.GROUP_TYPE) {
                            Intent intent = new Intent(context, GroupChatHolderActivity.class);
                            intent.putExtra("GroupId", data.getGroupId());
                            intent.putExtra("GroupName", data.getName());
                            intent.putExtra("GroupProfile", data.getImageLink());
                            context.startActivity(intent);

                        } else {

                            Intent intent = new Intent(context, UserChatHolderActivity.class);
                            intent.putExtra("HisUID", data.getUid());
                            intent.putExtra("HisName", data.getName());
                            intent.putExtra("HisProfile", data.getImageLink());
                            intent.putExtra("HisType", data.getUserType());
                            context.startActivity(intent);
                        }
                    }
                });
        }
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
        if(type == 1) {
            return list.size();
        } else {
            return list2.size();
        }
    }
}

