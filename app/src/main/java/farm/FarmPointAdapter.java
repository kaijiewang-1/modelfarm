package farm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class FarmPointAdapter extends RecyclerView.Adapter<FarmPointAdapter.FarmPointViewHolder> {

    private List<farm_detail.FarmPoint> pointList;
    private OnPointClickListener listener;

    public interface OnPointClickListener {
        void onPointClick(farm_detail.FarmPoint point);
    }

    public FarmPointAdapter(List<farm_detail.FarmPoint> pointList, OnPointClickListener listener) {
        this.pointList = pointList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FarmPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_farm_point, parent, false);
        return new FarmPointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmPointViewHolder holder, int position) {
        farm_detail.FarmPoint point = pointList.get(position);
        holder.bind(point);
    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    class FarmPointViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardPoint;
        private TextView tvPointName;
        private TextView tvPointArea;
        private TextView tvPointCrop;
        private TextView tvPointTime;

        public FarmPointViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPoint = itemView.findViewById(R.id.card_point);
            tvPointName = itemView.findViewById(R.id.tv_point_name);
            tvPointArea = itemView.findViewById(R.id.tv_point_area);
            tvPointCrop = itemView.findViewById(R.id.tv_point_crop);
            tvPointTime = itemView.findViewById(R.id.tv_point_time);
        }

        public void bind(farm_detail.FarmPoint point) {
            tvPointName.setText(point.getName());
            tvPointArea.setText("面积：" + point.getArea());
            tvPointCrop.setText("种植作物：" + point.getCrop());
            tvPointTime.setText("建成时间：" + point.getBuildTime());

            cardPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPointClick(point);
                    }
                }
            });
        }
    }
}
