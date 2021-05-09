package com.blackdev.thaparhelper.dashboard.Chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.dashboard.Chat.ChatBaseViewHolder;
import com.blackdev.thaparhelper.dashboard.Chat.Models.ModelGroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupChatAdapter extends RecyclerView.Adapter<ChatBaseViewHolder> {
    ArrayList<HashMap<String, Object>> list;
    final Context context;
    final String uid = FirebaseAuth.getInstance().getUid();
    final String groupId;

    public GroupChatAdapter(ArrayList<HashMap<String, Object>> list, Context context, String groupId) {
        this.list = list;
        this.context = context;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public ChatBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType>10) {
            switch (viewType-10) {
                case Constants.NORMAL_MESSAGE:
                        view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
                        return new normalChatRight(view);
                case Constants.ASSIGNMENT_MESSAGE:
                    view = LayoutInflater.from(context).inflate(R.layout.row_assignment_right, parent, false);
                    return new AssignmentChatRight(view);
//                case Constants.IMAGE_MESSAGE:
//                    view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
//                    return new normalChatRight(view);
//                case Constants.DOC_MESSAGE:
//                    view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
//                    return new normalChatRight(view);

            }
        } else {
            switch (viewType) {
                case Constants.NORMAL_MESSAGE:
                    view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
                    return new normalChatLeft(view);
                case Constants.ASSIGNMENT_MESSAGE:
                    view = LayoutInflater.from(context).inflate(R.layout.row_assignment_left, parent, false);
                    return new AssignmentChatLeft(view);
//                case Constants.IMAGE_MESSAGE:
//                    view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
//                    return new normalChatRight(view);
//                case Constants.DOC_MESSAGE:
//                    view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
//                    return new normalChatRight(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(((String)list.get(position).get(context.getString(R.string.SenderUid))).equals(uid)) {
            return (int)list.get(position).get(context.getString(R.string.MessageType)) + 10;
        }
        return (int)list.get(position).get(context.getString(R.string.MessageType));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class normalChatLeft extends ChatBaseViewHolder {
        final TextView senderNameTextView;
        final TextView messageTextView;
        //final TextView timeTextView;


        public normalChatLeft(@NonNull View itemView) {
            super(itemView);
            senderNameTextView = itemView.findViewById(R.id.nameChatLeftTV);
            messageTextView = itemView.findViewById(R.id.messageLeftTV);
            senderNameTextView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            final String messageId = (String) list.get(position).get(context.getString(R.string.messageId));
            final ModelGroupChat data = (ModelGroupChat) list.get(position).get(context.getString(R.string.Data));
            final int groupSize = (int) list.get(position).get(context.getString(R.string.groupCount));
            senderNameTextView.setText(data.getSenderName());
            messageTextView.setText(data.getMessage());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child(context.getString(R.string.new_messages)).child(messageId);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                //final int seenCount = new
                                        //TODO: update seenCount or delete the message
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

    public class  normalChatRight  extends ChatBaseViewHolder {

        public normalChatRight(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }
    }

    public class AssignmentChatLeft extends ChatBaseViewHolder {

        public AssignmentChatLeft(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }
    }

    public class AssignmentChatRight extends ChatBaseViewHolder {

        public AssignmentChatRight(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }
    }
}
