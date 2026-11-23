package com.cardmaster.app.ui.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.data.local.entity.NotificationEntity;
import com.cardmaster.app.databinding.ItemNotificationBinding;
import com.cardmaster.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationEntity notification);
        void onDeleteClick(NotificationEntity notification);
    }

    private final Context context;
    private final OnNotificationClickListener listener;
    private List<NotificationEntity> notifications = new ArrayList<>();

    public NotificationAdapter(Context context, OnNotificationClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setNotifications(List<NotificationEntity> list) {
        this.notifications = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotifViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotifViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        public NotifViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationEntity notif) {
            binding.tvNotifTitle.setText(notif.getTitle());
            binding.tvNotifMessage.setText(notif.getMessage());
            binding.tvNotifTime.setText(DateUtils.formatDateTime(notif.getCreatedAt()));

            binding.vUnreadDot.setVisibility(notif.isRead() ? View.GONE : View.VISIBLE);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onNotificationClick(notif);
            });

            binding.btnDeleteNotif.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(notif);
            });
        }
    }
}
