package farm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.example.modelfarm.network.models.FarmSite;

import java.util.List;

public class FarmPointAdapter extends RecyclerView.Adapter<FarmPointAdapter.FarmPointViewHolder> {

    private List<FarmSite> farmSiteList;
    private OnPointClickListener listener;

    public interface OnPointClickListener {
        void onPointClick(FarmSite farmSite);
        void onPointDelete(FarmSite farmSite);
    }

    public FarmPointAdapter(List<FarmSite> farmSiteList, OnPointClickListener listener) {
        this.farmSiteList = farmSiteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FarmPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm_point, parent, false);
        return new FarmPointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmPointViewHolder holder, int position) {
        FarmSite farmSite = farmSiteList.get(position);
        holder.bind(farmSite);
    }

    @Override
    public int getItemCount() {
        return farmSiteList.size();
    }

    class FarmPointViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPointName;
        private TextView tvPointArea;
        private TextView tvPointCrop;
        private TextView tvPointTime;
        private ImageView btnDeletePoint;

        public FarmPointViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPointName = itemView.findViewById(R.id.tv_point_name);
            tvPointArea = itemView.findViewById(R.id.tv_point_area);
            tvPointCrop = itemView.findViewById(R.id.tv_point_crop);
            tvPointTime = itemView.findViewById(R.id.tv_point_time);
            btnDeletePoint = itemView.findViewById(R.id.btn_delete_point);
        }

        public void bind(FarmSite farmSite) {
            tvPointName.setText(farmSite.getName());
            
            // Display capacity from properties
            String capacity = "容量：";
            if (farmSite.getProperties() != null && farmSite.getProperties().get("capacity") != null) {
                capacity += farmSite.getProperties().get("capacity");
            } else {
                capacity += "-";
            }
            tvPointArea.setText(capacity);
            
            // Display type from properties
            String type = "类型：";
            if (farmSite.getProperties() != null && farmSite.getProperties().get("type") != null) {
                type += farmSite.getProperties().get("type");
            } else {
                type += "-";
            }
            tvPointCrop.setText(type);
            
            // Display livestock count if available
            if (farmSite.getSum() != 0) {
                tvPointArea.setText("容量：" + farmSite.getSum());
            }
            
            tvPointTime.setText("创建时间：" + farmSite.getCreatedAt());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPointClick(farmSite);
                }
            });

            btnDeletePoint.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPointDelete(farmSite);
                }
            });
        }
    }
}
