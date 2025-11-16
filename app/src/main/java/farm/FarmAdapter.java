package farm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {

    private final List<farm_list.Farm> farmList;
    private final OnFarmClickListener listener;

    public interface OnFarmClickListener {
        void onFarmClick(farm_list.Farm farm);
        void onFarmDelete(farm_list.Farm farm);
    }

    public FarmAdapter(List<farm_list.Farm> farmList, OnFarmClickListener listener) {
        this.farmList = farmList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_farm, parent, false);
        return new FarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {
        farm_list.Farm farm = farmList.get(position);
        holder.bind(farm);
    }

    @Override
    public int getItemCount() {
        return farmList.size();
    }

    class FarmViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardFarm;
        private final TextView tvFarmName;
        private final TextView tvFarmLocation;
        private final TextView tvFarmArea;
        private final TextView tvFarmType;
        private final TextView tvFarmTemperature;
        private final TextView tvFarmHumidity;
        private final TextView tvFarmStatus;
        private final TextView tvFarmDescription;
        private final ImageButton btnDelete;

        public FarmViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFarm = itemView.findViewById(R.id.card_farm);
            tvFarmName = itemView.findViewById(R.id.tv_farm_name);
            tvFarmLocation = itemView.findViewById(R.id.tv_farm_location);
            tvFarmArea = itemView.findViewById(R.id.tv_farm_area);
            tvFarmType = itemView.findViewById(R.id.tv_farm_type);
            tvFarmTemperature = itemView.findViewById(R.id.tv_farm_temperature);
            tvFarmHumidity = itemView.findViewById(R.id.tv_farm_humidity);
            tvFarmStatus = itemView.findViewById(R.id.tv_farm_status);
            tvFarmDescription = itemView.findViewById(R.id.tv_farm_description);
            btnDelete = itemView.findViewById(R.id.btn_delete_farm);
        }

        public void bind(farm_list.Farm farm) {
            tvFarmName.setText(farm.getName());
            tvFarmLocation.setText(farm.getLocation());
            tvFarmArea.setText(farm.getArea());
            tvFarmType.setText(farm.getType());
            tvFarmTemperature.setText(farm.getTemperature());
            tvFarmHumidity.setText(farm.getHumidity());
            tvFarmStatus.setText(farm.getStatus());
            tvFarmDescription.setText(farm.getDescription());

            // 根据状态设置颜色
            if ("正常".equals(farm.getStatus())) {
                tvFarmStatus.setTextColor(0xFF4CAF50);
            } else {
                tvFarmStatus.setTextColor(0xFFF44336);
            }

            cardFarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onFarmClick(farm);
                    }
                }
            });

            // 设置删除按钮监听器
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onFarmDelete(farm);
                    }
                }
            });
        }
    }
}
