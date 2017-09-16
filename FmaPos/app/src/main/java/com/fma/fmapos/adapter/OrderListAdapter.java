package com.fma.fmapos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fma.fmapos.R;
import com.fma.fmapos.helper.CurrencyHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelOrder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<ModelOrder> orders;

    public OrderListAdapter(@NonNull Context context, List<ModelOrder> orders) {
        this.context = context;
        this.orders = orders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.order_list_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelOrder = orders.get(i);
        viewHolder.txtOrderNo.setText(viewHolder.modelOrder.getOrderno());

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy H:mm", new Locale("id", "ID"));

        viewHolder.txtOrderDate.setText(formatter.format(viewHolder.modelOrder.getOrderdate()));
        viewHolder.txtOrderAmount.setText(CurrencyHelper.format(viewHolder.modelOrder.getAmount()));

    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ModelOrder modelOrder;
        TextView txtOrderNo;
        TextView txtOrderDate;
        TextView txtOrderAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderNo = (TextView) itemView.findViewById(R.id.txtOrderNo);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtOrderAmount = (TextView) itemView.findViewById(R.id.txtOrderAmount);
        }
    }
}





