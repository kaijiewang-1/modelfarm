package com.example.modelfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.card.MaterialCardView;

import com.example.modelfarm.network.models.Device;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<Device> deviceList;
    private OnDeviceClickListener listener;

    public interface OnDeviceClickListener {
        void onDeviceClick(Device device);
        void onDeviceDelete(Device device);
    }

    public DeviceAdapter(List<Device> deviceList, OnDeviceClickListener listener) {
        this.deviceList = deviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.bind(device, listener);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardDevice;
        private ImageView ivDeviceIcon;
        private TextView tvDeviceName;
        private TextView tvDeviceStatus;
        private TextView tvDeviceValue;
        private ImageButton btnDelete;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDevice = itemView.findViewById(R.id.card_device);
            ivDeviceIcon = itemView.findViewById(R.id.iv_device_icon);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
            tvDeviceStatus = itemView.findViewById(R.id.tv_device_status);
            tvDeviceValue = itemView.findViewById(R.id.tv_device_value);
            btnDelete = itemView.findViewById(R.id.btn_delete_device);
        }

        public void bind(Device device, OnDeviceClickListener listener) {
            ivDeviceIcon.setImageResource(device.getIconResource());
            tvDeviceName.setText(device.getName());
            tvDeviceStatus.setText(device.getStatusText());
            tvDeviceValue.setText(device.getDisplayValue());

            // 根据设备状态设置颜色
            if ("在线".equals(device.getStatusText())) {
                tvDeviceStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvDeviceStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }

            // 设置点击监听器
            cardDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeviceClick(device);
                    }
                }
            });

            // 设置删除按钮监听器
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeviceDelete(device);
                    }
                }
            });
        }
    }
}
