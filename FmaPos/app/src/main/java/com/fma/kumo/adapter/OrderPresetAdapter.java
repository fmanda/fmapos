package com.fma.kumo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.model.ModelOrderPreset;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderPresetAdapter extends RecyclerView.Adapter<OrderPresetAdapter.ViewHolder> {
    private Context context;
    private List<ModelOrderPreset> modelOrderPresets;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public OrderPresetAdapter(Context context, List<ModelOrderPreset> modelOrderPresets) {
        this.context = context;
        this.modelOrderPresets = modelOrderPresets;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.orderpreset_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelOrderPreset = modelOrderPresets.get(i);
        viewHolder.txtOrderPreset.setText(viewHolder.modelOrderPreset.getName());
    }

    @Override
    public int getItemCount() {
        return modelOrderPresets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelOrderPreset modelOrderPreset;
        public TextView txtOrderPreset;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderPreset = (TextView) itemView.findViewById(R.id.txtOrderPreset);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}


