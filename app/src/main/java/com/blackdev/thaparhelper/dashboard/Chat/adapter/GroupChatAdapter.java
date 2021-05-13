package com.blackdev.thaparhelper.dashboard.Chat.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
            //final String messageId = (String) list.get(position).get(context.getString(R.string.messageId));
            final ModelGroupChat data = (ModelGroupChat) list.get(position).get(context.getString(R.string.Data));
            //final int groupSize = (int) list.get(position).get(context.getString(R.string.groupCount));
            senderNameTextView.setText(data.getSenderName());
            messageTextView.setText(data.getMessage());
        }
    }

    public class  normalChatRight  extends ChatBaseViewHolder {
        final TextView messageTextView;
        public normalChatRight(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageRightTV);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            final ModelGroupChat data = (ModelGroupChat) list.get(position).get(context.getString(R.string.Data));
            messageTextView.setText(data.getMessage());
        }
    }

    public class AssignmentChatLeft extends ChatBaseViewHolder {
        TextView subjectTV,topicTV,descriptionTV,deadlineTV, senderNameTV;
        ImageButton assignmentPDFButton;
        Button submissionLinkButton;

        public AssignmentChatLeft(@NonNull View itemView) {
            super(itemView);
            senderNameTV = itemView.findViewById(R.id.nameChatLeftTV);
            topicTV = itemView.findViewById(R.id.row_message_topic_name);
            subjectTV = itemView.findViewById(R.id.row_message_subject_name);
            descriptionTV = itemView.findViewById(R.id.row_message_desc);
            deadlineTV = itemView.findViewById(R.id.row_message_deadline);
            submissionLinkButton = itemView.findViewById(R.id.row_message_submit);
            assignmentPDFButton = itemView.findViewById(R.id.row_message_pdf_button);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            final HashMap<String, Object> data = (HashMap<String, Object>) list.get(position).get(context.getString(R.string.Data));
            subjectTV.setText((String)data.get(context.getString(R.string.subjectName)));
            senderNameTV.setText((String) data.get(context.getString(R.string.senderName)));
            topicTV.setText((String)data.get(context.getString(R.string.assignmentTopic)));
            descriptionTV.setText((String)data.get(context.getString(R.string.assignmentDesc)));
            deadlineTV.setText((String)data.get(context.getString(R.string.assignmentDeadline)));
            submissionLinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show webview to submit
                }
            });
            final String fileLink = (String)data.get(context.getString(R.string.fileLink));
            assignmentPDFButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    public class AssignmentChatRight extends ChatBaseViewHolder {

        TextView subjectTV,topicTV,descriptionTV,deadlineTV,submissionLinkTV;
        ImageButton assignmentPDFButton;
        Button copyLinkButton;


        public AssignmentChatRight(@NonNull View itemView) {
            super(itemView);
            topicTV = itemView.findViewById(R.id.row_message_topic_name);
            subjectTV = itemView.findViewById(R.id.row_message_subject_name);
            descriptionTV = itemView.findViewById(R.id.row_message_desc);
            deadlineTV = itemView.findViewById(R.id.row_message_deadline);
            submissionLinkTV = itemView.findViewById(R.id.row_message_submission_link);
            assignmentPDFButton = itemView.findViewById(R.id.row_message_pdf_button);
            copyLinkButton = itemView.findViewById(R.id.row_message_copy_link);
        }


        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            final HashMap<String, Object> data = (HashMap<String, Object>) list.get(position).get(context.getString(R.string.Data));
            subjectTV.setText((String)data.get(context.getString(R.string.subjectName)));
            topicTV.setText((String)data.get(context.getString(R.string.assignmentTopic)));
            descriptionTV.setText((String)data.get(context.getString(R.string.assignmentDesc)));
            deadlineTV.setText((String)data.get(context.getString(R.string.assignmentDeadline)));
            submissionLinkTV.setText((String)data.get(context.getString(R.string.submissionLink)));
            final String fileLink = (String)data.get(context.getString(R.string.fileLink));

            assignmentPDFButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show web view
                }
            });
            copyLinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(context.getString(R.string.submissionLink), submissionLinkTV.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
