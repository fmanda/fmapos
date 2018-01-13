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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yy", new Locale("id", "ID"));
        SimpleDateFormat tmf = new SimpleDateFormat("H:mm", new Locale("id", "ID"));
        if (i == 0) {
            viewHolder.modelReconcile = null;
            viewHolder.txtDate.setText(dtf.format(new Date()));
            viewHolder.txtTime.setText("-");
            viewHolder.txtStatus.setText("OPEN");
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorLightGreen));
        }else{
            viewHolder.modelReconcile = reconcileList.get(i-1);
            viewHolder.txtDate.setText(dtf.format(viewHolder.modelReconcile.getTransdate()));
            viewHolder.txtTime.setText(tmf.format(viewHolder.modelReconcile.getTransdate()));
            viewHolder.txtStatus.setText("CLOSED");
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorLightRed));
        }
   }

    @Override
    public int getItemCount() {
        return reconcileList.size() + 1;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelReconcile modelReconcile;
        public TextView txtDate;
        public TextView txtTime;
        public TextView txtStatus;
//        public LinearLayout layoutCashTrans;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
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


