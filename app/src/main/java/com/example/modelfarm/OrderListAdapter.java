package com.example.modelfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.models.Order;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private final List<Order> orderList;
    private final OnOrderActionListener actionListener;

    public OrderListAdapter(List<Order> orderList) {
        this(orderList, null);
    }

    public OrderListAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvStatus;
        private final TextView tvCreatedAt;
        private final TextView tvDescription;
        private final TextView tvId;
        private final android.widget.ImageButton btnDelete;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Order o) {
            tvId.setText("#" + o.getId());
            tvTitle.setText(o.getTitle());
            tvDescription.setText(o.getDescription());
            tvCreatedAt.setText(o.getCreatedAt());
            tvStatus.setText(statusText(o.getStatus()));

            if (btnDelete != null) {
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (actionListener != null) {
                            actionListener.onDelete(o);
                        }
                    }
                });
            }
        }

        private String statusText(int status) {
            switch (status) {
                case 1: return "待处理";
                case 2: return "已完成";
                case 3: return "加急";
                default: return String.valueOf(status);
            }
        }
    }

    public interface OnOrderActionListener {
        void onDelete(Order order);
    }
}


