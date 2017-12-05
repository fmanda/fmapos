package com.fma.kumo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.model.ModelOrderCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Context context;
    private List<ModelOrderCategory>  categories;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CategoryListAdapter(Context context, List<ModelOrderCategory> categories) {
        this.context = context;
        this.categories = categories;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.category_pick_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.category = categories.get(i);
        viewHolder.btnCategory.setText(viewHolder.category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelOrderCategory category;
        public Button btnCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            btnCategory = (Button) itemView.findViewById(R.id.btnCategory);
            btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
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


