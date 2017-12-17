package com.fma.kumo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.helper.CurrencyHelper;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<ModelOrder> orders;
    private VoidListener mVoidListener;
//    private ItemClickListener mClickListener;

    public OrderHistoryAdapter(@NonNull Context context, List<ModelOrder> orders) {
        this.context = context;
        this.orders = orders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.order_history_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parent = this;
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelOrder = orders.get(i);
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm", new Locale("id", "ID"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yy", new Locale("id", "ID"));
//        SimpleDateFormat formatter = new SimpleDateFormat("H:mm", new Locale("id", "ID"));

        viewHolder.txtOrderTime.setText(timeFormat.format(viewHolder.modelOrder.getOrderdate()));
        viewHolder.txtOrderDate.setText(dateFormat.format(viewHolder.modelOrder.getOrderdate()));
        viewHolder.txtOrderAmount.setText(CurrencyHelper.format(viewHolder.modelOrder.getAmount()));

        if (viewHolder.modelOrder.getCardpayment()>0){
            viewHolder.txtStatus.setText("KARTU");
        }else{
            viewHolder.txtStatus.setText("TUNAI");
        }

        viewHolder.pnlVoid.setVisibility(View.GONE);
        if (viewHolder.modelOrder.getStatus() < 2){
            if (viewHolder.isVoidVisible) {
                viewHolder.pnlVoid.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.txtStatus.setText(viewHolder.modelOrder.getStatusString());
        if (viewHolder.modelOrder.getStatus() == 2 ){
            viewHolder.txtStatus.setTextColor(Color.RED);
        }else{
            viewHolder.txtStatus.setTextColor(Color.DKGRAY);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ModelOrder modelOrder;
        TextView txtTipeBayar;
        TextView txtOrderTime;
        TextView txtOrderDate;
        TextView txtOrderAmount;
        TextView txtStatus;
        LinearLayout pnlVoid;
        Boolean isVoidVisible;
        Button btnVoid;
        OrderHistoryAdapter parent;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtTipeBayar = (TextView) itemView.findViewById(R.id.txtTipeBayar);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtOrderTime = (TextView) itemView.findViewById(R.id.txtOrderTime);
            txtOrderAmount = (TextView) itemView.findViewById(R.id.txtOrderAmount);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            pnlVoid = (LinearLayout) itemView.findViewById(R.id.pnlVoid);
            btnVoid = (Button) itemView.findViewById(R.id.btnVoid);
            isVoidVisible = false;
            itemView.setOnClickListener(this);
            btnVoid.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

//            Toast.makeText(v.getContext(), "Test", Toast.LENGTH_SHORT).show();
            if (v.getId() != R.id.btnVoid) {
                this.isVoidVisible = !this.isVoidVisible;
                this.parent.notifyDataSetChanged();
            }else{
                if (mVoidListener != null) mVoidListener.onVoidClick(modelOrder);
            }
        }

//        @Override
//        public void onVoidClick(ModelOrder modelOrder) {
//
//        }
    }

    // allows clicks events to be caught
//    public void setClickListener(OrderHistoryAdapter.ItemClickListener mClickListener) {
//        this.mClickListener = mClickListener;
//    }

    public void setVoidListener(OrderHistoryAdapter.VoidListener mVoidListener) {
        this.mVoidListener = mVoidListener;
    }

    // parent activity will implement this method to respond to click events
    public interface VoidListener {
        void onVoidClick(ModelOrder modelOrder);
    }
//
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
}





