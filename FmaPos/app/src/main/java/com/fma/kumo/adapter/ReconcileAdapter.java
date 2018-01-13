package com.fma.kumo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fma.kumo.R;
import com.fma.kumo.model.ModelReconcile;
import java.util.Date;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class ReconcileAdapter extends RecyclerView.Adapter<ReconcileAdapter.ViewHolder> {
    private Context context;
    private List<ModelReconcile> reconcileList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ReconcileAdapter(Context context, List<ModelReconcile> reconcileList) {
        this.context = context;
        this.reconcileList = reconcileList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.reconcile_list_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i == 0) {
            viewHolder.modelReconcile = null;
            viewHolder.txtDate.setText((new Date()).toString());
            viewHolder.txtStatus.setText("OPEN");
            viewHolder.txtStatus.setTextColor(Color.GREEN);
        }else{
            viewHolder.modelReconcile = reconcileList.get(i);
            viewHolder.txtDate.setText(viewHolder.modelReconcile.getTransdate().toString());
            viewHolder.txtStatus.setText("CLOSED");
            viewHolder.txtStatus.setTextColor(Color.RED);
        }
   }

    @Override
    public int getItemCount() {
        return reconcileList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelReconcile modelReconcile;
        public TextView txtDate;
        public TextView txtStatus;
//        public LinearLayout layoutCashTrans;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
//            layoutCashTrans = (LinearLayout) itemView.findViewById(R.id.layoutCashTrans);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}


