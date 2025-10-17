package com.example.modelfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * 通知列表适配器
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<MessageNotificationActivity.Notification> notificationList;

    public NotificationAdapter(List<MessageNotificationActivity.Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        MessageNotificationActivity.Notification notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardNotification;
        private final TextView tvFarmName;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final TextView tvLevel;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNotification = itemView.findViewById(R.id.cardNotification);
            tvFarmName = itemView.findViewById(R.id.tvFarmName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }

        public void bind(MessageNotificationActivity.Notification notification) {
            tvFarmName.setText(notification.getFarmName());
            tvMessage.setText(notification.getMessage());
            tvTime.setText(notification.getTime());
            tvLevel.setText(notification.getLevel());

            // 设置通知级别颜色
            switch (notification.getLevel()) {
                case "high":
                    tvLevel.setTextColor(0xFFF44336);
                    cardNotification.setStrokeColor(0xFFF44336);
                    break;
                case "medium":
                    tvLevel.setTextColor(0xFFFF9800);
                    cardNotification.setStrokeColor(0xFFFF9800);
                    break;
                case "low":
                    tvLevel.setTextColor(0xFF4CAF50);
                    cardNotification.setStrokeColor(0xFF4CAF50);
                    break;
            }

            // 设置已读状态
            if (notification.isRead()) {
                cardNotification.setAlpha(0.6f);
            } else {
                cardNotification.setAlpha(1.0f);
            }
        }
    }
}
