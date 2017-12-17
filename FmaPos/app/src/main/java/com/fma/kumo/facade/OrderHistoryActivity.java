package com.fma.kumo.facade;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.adapter.OrderHistoryAdapter;
import com.fma.kumo.controller.ControllerOrder;
import com.fma.kumo.model.ModelOrder;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderHistoryActivity extends BaseActivity {
    List<ModelOrder> orders;
    ControllerOrder controllerOrder;
    OrderHistoryAdapter orderListAdapter;
    RecyclerView rvOrders;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_order_history, this.mainframe);

        rvOrders = (RecyclerView) findViewById(R.id.rvOrders);
        controllerOrder = new ControllerOrder(this);
        loadOrders();
    }

    public void loadOrders(){
        orders = controllerOrder.getOrderList(Boolean.FALSE);
        orderListAdapter = new OrderHistoryAdapter(this, orders);

        orderListAdapter.setVoidListener(new OrderHistoryAdapter.VoidListener() {

            @Override
            public void onVoidClick(ModelOrder modelOrder) {
//                Toast.makeText(OrderHistoryActivity.this, modelOrder.getOrderno(), Toast.LENGTH_SHORT).show();
                VoidOrder(modelOrder);
            }
        });
        rvOrders.setAdapter(orderListAdapter);
        rvOrders.setLayoutManager(new GridLayoutManager(this, 1));
    }

    public void VoidOrder(final ModelOrder modelOrder){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Anda yakin melakukan void atas Order : " + modelOrder.getOrderno());
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controllerOrder.CreateVoid(modelOrder);
                        loadOrders();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
