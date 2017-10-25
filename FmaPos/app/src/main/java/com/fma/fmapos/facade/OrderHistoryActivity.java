package com.fma.fmapos.facade;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.OrderHistoryAdapter;
import com.fma.fmapos.adapter.OrderHoldAdapter;
import com.fma.fmapos.controller.ControllerOrder;
import com.fma.fmapos.model.ModelOrder;

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
        rvOrders.setAdapter(orderListAdapter);
        rvOrders.setLayoutManager(new GridLayoutManager(this, 1));
    }


}
