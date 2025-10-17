package company;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * 企业列表适配器
 */
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private final List<CompanyListActivity.Company> companyList;
    private final OnCompanyClickListener listener;

    public interface OnCompanyClickListener {
        void onCompanyClick(CompanyListActivity.Company company);
    }

    public CompanyAdapter(List<CompanyListActivity.Company> companyList, OnCompanyClickListener listener) {
        this.companyList = companyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyListActivity.Company company = companyList.get(position);
        holder.bind(company, listener);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    static class CompanyViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardCompany;
        private final TextView tvCompanyName;
        private final TextView tvCompanyStatus;
        private final ImageView ivCheckIcon;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCompany = itemView.findViewById(R.id.cardCompany);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvCompanyStatus = itemView.findViewById(R.id.tvCompanyStatus);
            ivCheckIcon = itemView.findViewById(R.id.ivCheckIcon);
        }

        public void bind(CompanyListActivity.Company company, OnCompanyClickListener listener) {
            tvCompanyName.setText(company.getName());
            tvCompanyStatus.setText(company.getStatus());
            
            // 设置选中状态
            if (company.isJoined()) {
                ivCheckIcon.setVisibility(View.VISIBLE);
                cardCompany.setStrokeColor(0xFF4CAF50);
                cardCompany.setStrokeWidth(2);
            } else {
                ivCheckIcon.setVisibility(View.GONE);
                cardCompany.setStrokeColor(0xFFE0E0E0);
                cardCompany.setStrokeWidth(1);
            }

            cardCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCompanyClick(company);
                    }
                }
            });
        }
    }
}
