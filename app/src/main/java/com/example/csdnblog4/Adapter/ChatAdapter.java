package com.example.csdnblog4.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.csdnblog4.Entity.ChatContent;
import com.example.csdnblog4.R;

import java.util.List;

/**
 * Created by orange on 16/6/12.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatContent> chatContentList;

    public ChatAdapter(Context context, List<ChatContent> chatContentList) {
        this.context = context;
        this.chatContentList = chatContentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ChatContent.GUEST) {
            return new GuestViewHolder(inflater.inflate(R.layout.left_chater, parent, false));
        } else if (viewType == ChatContent.MYSELF) {
            return new MySelfViewHolder(inflater.inflate(R.layout.right_chater, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (chatContentList == null)
            return;
        if (holder instanceof MySelfViewHolder) {
            MySelfViewHolder mySelfViewHolder = (MySelfViewHolder) holder;
            mySelfViewHolder.tvMyself.setText(chatContentList.get(position).getChatContent());
        } else if (holder instanceof GuestViewHolder) {
            GuestViewHolder guestViewHolder = (GuestViewHolder) holder;
            guestViewHolder.tvGuest.setText(chatContentList.get(position).getChatContent());
        }
    }

    @Override
    public int getItemCount() {
        if (chatContentList != null)
            return chatContentList.size();
        else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatContentList != null)
            return chatContentList.get(position).getChatType();
        else {
            return 0;
        }
    }

    public void addMessage(ChatContent chatContent) {
        if (chatContentList == null) {
            return;
        }
        chatContentList.add(chatContent);
        notifyItemInserted(chatContentList.size());
    }

    class MySelfViewHolder extends RecyclerView.ViewHolder {

        TextView tvMyself;

        public MySelfViewHolder(View itemView) {
            super(itemView);
            tvMyself = (TextView) itemView.findViewById(R.id.tv_right_messge);
        }
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView tvGuest;

        public GuestViewHolder(View itemView) {
            super(itemView);
            tvGuest = (TextView) itemView.findViewById(R.id.id_tv_left_message);
        }
    }
}
