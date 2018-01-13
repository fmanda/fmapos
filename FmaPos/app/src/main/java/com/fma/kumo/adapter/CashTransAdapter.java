package com.fma.kumo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelCustomer;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class CashTransAdapter extends RecyclerView.Adapter<CashTransAdapter.ViewHolder> {
    private Context context;
    private List<ModelCashTrans> cashTrans;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    public CashTransAdapter(Context context, List<ModelCashTrans> cashTrans) {
        this.context = context;
        this.cashTrans = cashTrans;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cashtrans_list_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.cashTrans = cashTrans.get(i);
        viewHolder.txtAmount.setText(viewHolder.cashTrans.getAmount().toString());
        viewHolder.txtNotes.setText(viewHolder.cashTrans.getNotes().toString());
        if (viewHolder.cashTrans.getAmount() > 0){
            viewHolder.txtAmount.setTextColor(context.getResources().getColor(R.color.colorLightGreen));
        }else{
            viewHolder.txtAmount.setTextColor(context.getResources().getColor(R.color.colorLightRed));
        }
    }

    @Override
    public int getItemCount() {
        return cashTrans.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelCashTrans cashTrans;
        public TextView txtAmount;
        public TextView txtNotes;
//        public LinearLayout layoutCashTrans;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtNotes = (TextView) itemView.findViewById(R.id.txtNotes);
//            layoutCashTrans = (LinearLayout) itemView.findViewById(R.id.layoutCashTrans);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }

    // allows clicks events to be caught
//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }


}


