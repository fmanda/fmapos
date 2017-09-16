package com.fma.fmapos.facade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.CustomerListAdapter;
import com.fma.fmapos.adapter.OrderListAdapter;
import com.fma.fmapos.controller.ControllerCustomer;
import com.fma.fmapos.controller.ControllerOrder;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelOrder;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderActivity extends BaseActivity {
    List<ModelOrder> orders;
    ControllerOrder controllerOrder;
    OrderListAdapter orderListAdapter;
    RecyclerView rvOrders;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_order, this.mainframe);

        rvOrders = (RecyclerView) findViewById(R.id.rvOrders);
        controllerOrder = new ControllerOrder(this);
        loadOrders();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateOrder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabOnClick();
            }
        });
    }

    public void loadOrders(){
        orders = controllerOrder.getOrderList();
        orderListAdapter = new OrderListAdapter(this, orders);
        rvOrders.setAdapter(orderListAdapter);
        rvOrders.setLayoutManager(new GridLayoutManager(this, 1));
    }

    public void fabOnClick(){
        startActivity(new Intent(this, OrderCreateActivity.class));
    }
}
