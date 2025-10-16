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

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private final List<EnvironmentMonitoringEnhancedActivity.Alert> alertList;

    public AlertAdapter(List<EnvironmentMonitoringEnhancedActivity.Alert> alertList) {
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        EnvironmentMonitoringEnhancedActivity.Alert alert = alertList.get(position);
        holder.bind(alert);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    class AlertViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardAlert;
        private final TextView tvAlertTitle;
        private final TextView tvAlertMessage;
        private final TextView tvAlertTime;
        private final TextView tvAlertLevel;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            cardAlert = itemView.findViewById(R.id.card_alert);
            tvAlertTitle = itemView.findViewById(R.id.tv_alert_title);
            tvAlertMessage = itemView.findViewById(R.id.tv_alert_message);
            tvAlertTime = itemView.findViewById(R.id.tv_alert_time);
            tvAlertLevel = itemView.findViewById(R.id.tv_alert_level);
        }

        public void bind(EnvironmentMonitoringEnhancedActivity.Alert alert) {
            tvAlertTitle.setText(alert.getTitle());
            tvAlertMessage.setText(alert.getMessage());
            tvAlertTime.setText(alert.getTime());
            tvAlertLevel.setText(alert.getLevel().toUpperCase());

            // 根据报警级别设置颜色
            switch (alert.getLevel()) {
                case "high":
                    tvAlertLevel.setTextColor(0xFFF44336);
                    cardAlert.setStrokeColor(0xFFF44336);
                    break;
                case "medium":
                    tvAlertLevel.setTextColor(0xFFFF9800);
                    cardAlert.setStrokeColor(0xFFFF9800);
                    break;
                case "low":
                    tvAlertLevel.setTextColor(0xFF4CAF50);
                    cardAlert.setStrokeColor(0xFF4CAF50);
                    break;
            }
        }
    }
}
