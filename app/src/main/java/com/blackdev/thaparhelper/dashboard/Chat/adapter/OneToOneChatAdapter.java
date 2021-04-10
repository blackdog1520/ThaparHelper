package com.blackdev.thaparhelper.dashboard.Chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.dashboard.Chat.ChatBaseViewHolder;
import com.blackdev.thaparhelper.database.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class OneToOneChatAdapter extends RecyclerView.Adapter<ChatBaseViewHolder>{


    private final static int LEFT_MESSAGE_TYPE = 0;
    private final static int RIGHT_MESSAGE_TYPE = 1;
    private final static int ASSIGNMENT_MESSAGE_TYPE = 3;
    private Context context;
    private List<ChatData> chatList;
    FirebaseUser firebaseUser;

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).isSentByMe()) {
            return RIGHT_MESSAGE_TYPE;
        } else {
            return LEFT_MESSAGE_TYPE;
        }

    }

    public OneToOneChatAdapter(Context context, List<ChatData> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LEFT_MESSAGE_TYPE :
                View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
                return new MessageLeftHolder(view);
            case RIGHT_MESSAGE_TYPE :
                View view2 = LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
                return new MessageRightHolder(view2);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBaseViewHolder holder, int position) {
        holder.onBind(position);


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class MessageLeftHolder extends ChatBaseViewHolder {

        TextView messageTV,Timestamp;
        public MessageLeftHolder(@NonNull View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.messageLeftTV);

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatData data = chatList.get(position);
            String message = data.getMessage();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(data.getTimeStamp()));
            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
            messageTV.setText(message);
        }

        @Override
        protected void clear() {

        }
    }

    class MessageRightHolder extends ChatBaseViewHolder {

        TextView messageTV,Timestamp;
        ImageView statusIV;
        public MessageRightHolder(@NonNull View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.messageRightTV);
            statusIV = itemView.findViewById(R.id.messageStatus);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatData data = chatList.get(position);
            String message = data.getMessage();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(data.getTimeStamp()));
            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
            messageTV.setText(message);
            if (data.isSeen()) {
                statusIV.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void clear() {

        }
    }




}
