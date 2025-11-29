package com.example.modelfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.models.Order;
import com.example.modelfarm.network.models.OrderStatus;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private final List<Order> orderList;
    private final OnOrderActionListener actionListener;
    private boolean isAdmin = false; // 是否为管理员

    public OrderListAdapter(List<Order> orderList) {
        this(orderList, null);
    }

    public OrderListAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.actionListener = listener;
    }
    
    /**
     * 设置管理员权限
     */
    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
        notifyDataSetChanged();
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
        private final com.google.android.material.button.MaterialButton btnClaim;
        private final com.google.android.material.button.MaterialButton btnDispatch;
        private final com.google.android.material.button.MaterialButton btnComplete;
        private final com.google.android.material.button.MaterialButton btnCheckIn;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnClaim = itemView.findViewById(R.id.btnClaim);
            btnDispatch = itemView.findViewById(R.id.btnDispatch);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
        }

        void bind(Order o) {
            tvId.setText("#" + o.getId());
            tvTitle.setText(o.getTitle());
            tvDescription.setText(o.getDescription());
            // 显示状态信息（发布/受理/完结）
            tvCreatedAt.setText(getStatusDetailText(o));
            tvStatus.setText(statusText(o.getStatus()));

            // Set up action buttons based on order status and acceptance state
            setupActionButtons(o);

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

            // Add click listener to view order details
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.content.Intent intent = new android.content.Intent(itemView.getContext(), OrderDetailActivity.class);
                    intent.putExtra("orderId", o.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void setupActionButtons(Order order) {
            // Reset all buttons visibility
            if (btnClaim != null) btnClaim.setVisibility(View.GONE);
            if (btnDispatch != null) btnDispatch.setVisibility(View.GONE);
            if (btnComplete != null) btnComplete.setVisibility(View.GONE);
            if (btnCheckIn != null) btnCheckIn.setVisibility(View.GONE);

            // Show appropriate buttons based on status
            switch (order.getStatus()) {
                case OrderStatus.PENDING:
                    // 待处理状态：显示认领和派发按钮
                    if (btnClaim != null) {
                        btnClaim.setVisibility(View.VISIBLE);
                        btnClaim.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onClaim(order);
                            }
                        });
                    }
                    if (btnDispatch != null) {
                        btnDispatch.setVisibility(View.VISIBLE);
                        btnDispatch.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onDispatch(order);
                            }
                        });
                    }
                    break;
                case OrderStatus.CLAIMED:
                    // 已认领状态：显示打卡和完成按钮
                    if (btnCheckIn != null) {
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckIn.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onCheckIn(order);
                            }
                        });
                    }
                    if (btnComplete != null) {
                        btnComplete.setVisibility(View.VISIBLE);
                        btnComplete.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onComplete(order);
                            }
                        });
                    }
                    // 只有管理员可以派发工单
                    if (isAdmin && btnDispatch != null) {
                        btnDispatch.setVisibility(View.VISIBLE);
                        btnDispatch.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onDispatch(order);
                            }
                        });
                    }
                    break;
                case OrderStatus.URGENT:
                    // 紧急状态：显示认领、派发和打卡按钮（如果已认领）
                    if (btnClaim != null) {
                        btnClaim.setVisibility(View.VISIBLE);
                        btnClaim.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onClaim(order);
                            }
                        });
                    }
                    if (btnDispatch != null) {
                        btnDispatch.setVisibility(View.VISIBLE);
                        btnDispatch.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onDispatch(order);
                            }
                        });
                    }
                    // 如果已认领，显示打卡按钮
                    if (order.isAccepted() == 1 && btnCheckIn != null) {
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckIn.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onCheckIn(order);
                            }
                        });
                    }
                    break;
            }
        }
        
        /**
         * 获取状态详细信息（发布/受理/完结）
         */
        private String getStatusDetailText(Order order) {
            StringBuilder sb = new StringBuilder();
            sb.append("发布: ").append(order.getCreatedAt());
            
            // 受理信息
            if (order.getAcceptedId() != null) {
                if (order.isAccepted() == 1) {
                    sb.append("\n受理: 已认领");
                } else {
                    sb.append("\n受理: 已派发");
                }
            }
            
            // 完结信息
            if (order.getCompletedAt() != null && !order.getCompletedAt().isEmpty()) {
                sb.append("\n完结: ").append(order.getCompletedAt());
            }
            
            return sb.toString();
        }

        private String statusText(int status) {
            switch (status) {
                case OrderStatus.PENDING:
                    return "待处理";
                case OrderStatus.COMPLETED:
                    return "已完成";
                case OrderStatus.URGENT:
                    return "加急";
                case OrderStatus.CLAIMED:
                    return "受理中";
                default:
                    return String.valueOf(status);
            }
        }
    }

    public interface OnOrderActionListener {
        void onDelete(Order order);

        void onClaim(Order order);

        void onDispatch(Order order);

        void onComplete(Order order);
        
        void onCheckIn(Order order);
    }
}
