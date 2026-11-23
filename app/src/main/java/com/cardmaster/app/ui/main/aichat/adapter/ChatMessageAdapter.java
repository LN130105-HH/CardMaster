package com.cardmaster.app.ui.main.aichat.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.databinding.ItemChatMessageBinding;
import com.cardmaster.app.ui.main.aichat.model.ChatMessage;
import com.cardmaster.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    private final Context context;
    private final List<ChatMessage> messages = new ArrayList<>();

    public ChatMessageAdapter(Context context) {
        this.context = context;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setMessages(List<ChatMessage> list) {
        messages.clear();
        if (list != null) messages.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatMessageBinding binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatMessageBinding binding;

        public MessageViewHolder(ItemChatMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatMessage message) {
            binding.tvMessageText.setText(message.getText());
            binding.tvMessageTime.setText(DateUtils.formatTime(message.getTimestamp()));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.tvMessageText.getLayoutParams();

            if (message.getType() == ChatMessage.TYPE_USER) {
                binding.llMessageContainer.setGravity(Gravity.END);
                binding.tvMessageText.setBackgroundResource(R.drawable.bg_chat_user);
                binding.tvMessageText.setTextColor(ContextCompat.getColor(context, R.color.color_on_primary));
                params.gravity = Gravity.END;
            } else {
                binding.llMessageContainer.setGravity(Gravity.START);
                binding.tvMessageText.setBackgroundResource(R.drawable.bg_chat_ai);
                binding.tvMessageText.setTextColor(ContextCompat.getColor(context, R.color.color_text_primary));
                params.gravity = Gravity.START;
            }
            binding.tvMessageText.setLayoutParams(params);
        }
    }
}
