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

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {

    private List<farm_list.Farm> farmList;
    private OnFarmClickListener listener;

    public interface OnFarmClickListener {
        void onFarmClick(farm_list.Farm farm);
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
        private MaterialCardView cardFarm;
        private TextView tvFarmName;
        private TextView tvFarmLocation;
        private TextView tvFarmArea;
        private TextView tvFarmDescription;

        public FarmViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFarm = itemView.findViewById(R.id.card_farm);
            tvFarmName = itemView.findViewById(R.id.tv_farm_name);
            tvFarmLocation = itemView.findViewById(R.id.tv_farm_location);
            tvFarmArea = itemView.findViewById(R.id.tv_farm_area);
            tvFarmDescription = itemView.findViewById(R.id.tv_farm_description);
        }

        public void bind(farm_list.Farm farm) {
            tvFarmName.setText(farm.getName());
            tvFarmLocation.setText(farm.getLocation());
            tvFarmArea.setText(farm.getArea());
            tvFarmDescription.setText(farm.getDescription());

            cardFarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onFarmClick(farm);
                    }
                }
            });
        }
    }
}
