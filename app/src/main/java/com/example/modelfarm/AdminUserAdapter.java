package com.example.modelfarm;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.models.UserWitnRole;

import java.util.List;

import android.widget.Button;

import java.util.ArrayList;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    private List<UserWitnRole> userList = new ArrayList<>();
    private OnDeleteClickListener deleteListener;

    // 定义点击事件接口
    public interface OnDeleteClickListener {
        void onDeleteClick(UserWitnRole user);
    }

    public AdminUserAdapter(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    // 设置新数据
    public void setData(List<UserWitnRole> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    // 删除单项数据并刷新（优化体验，不需要全量刷新）
    public void removeItem(UserWitnRole item) {
        int position = userList.indexOf(item);
        if (position != -1) {
            userList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserWitnRole item = userList.get(position);

        holder.tvUsername.setText(item.getUser().getUsername());
        holder.tvRole.setText(item.getRole());

        // 绑定删除按钮事件
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder 内部类
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRole;
        Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}