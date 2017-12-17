package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.adapter.OrderHoldAdapter;
import com.fma.kumo.controller.ControllerOrder;
import com.fma.kumo.facade.fragment.CreateCustomerFragment;
import com.fma.kumo.facade.fragment.PickCategoryFragment;
import com.fma.kumo.facade.fragment.PickCustomerFragment;
import com.fma.kumo.facade.fragment.PresetCategoryFragment;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelOrderCategory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class OrderActivity extends BaseActivity {
    List<ModelOrder> orders;
    ControllerOrder controllerOrder;
    OrderHoldAdapter orderHoldAdapter;
    RecyclerView rvOrders;
    ModelOrder modelOrder = null;

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

    private void loadOrders(){
        orders = controllerOrder.getOrderList(Boolean.TRUE);
        orderHoldAdapter = new OrderHoldAdapter(this, orders);
        rvOrders.setAdapter(orderHoldAdapter);
        rvOrders.setLayoutManager(new GridLayoutManager(this, 1));

        orderHoldAdapter.SetSelectHoldOrderListener(new OrderHoldAdapter.SelectHoldOrderListener() {
            @Override
            public void onSelectHoldOrder(ModelOrder modelOrder) {
                loadOrder(modelOrder);
            }
        });
    }

    private void loadOrder(ModelOrder modelOrder) {
        Intent intent = new Intent(this, OrderCreateActivity.class);
        if (modelOrder != null)
            intent.putExtra("modelOrder", modelOrder);
        startActivity(intent);
    }

    private void fabOnClick(){
        selectOrderCategory();
//        startActivity(new Intent(this, OrderCreateActivity.class));
    }

    private void selectOrderCategory(){
        List<ModelOrderCategory> categories= new ControllerOrder(this).getOrderCategory();
        if (!categories.isEmpty()) {
            FragmentManager fm = getFragmentManager();
            PickCategoryFragment pickCategoryFragment = new PickCategoryFragment();
            pickCategoryFragment.SetCategorySelectListener(new PickCategoryFragment.CategorySelectListener() {
                @Override
                public void OnSelectCategory(ModelOrderCategory modelOrderCategory) {
//                    Toast.makeText(OrderActivity.this, modelOrderCategory.getName(), Toast.LENGTH_SHORT).show();
                    createOrderByCategory(modelOrderCategory);
                }
            });
            pickCategoryFragment.show(fm, "Set Order Category");
        }else{
            selectCustomer();
        }
    }

    private void createOrderByCategory(ModelOrderCategory category){
        if (category == null){
            selectCustomer();
        }else if (category.IsReguler()){
            selectCustomer();
        }else{
            showDialogPresetCategory(category);
        }

    }

    private void showDialogPresetCategory(final ModelOrderCategory category) {
        FragmentManager fm = getFragmentManager();
        PresetCategoryFragment presetCategoryFragment = new PresetCategoryFragment();
        presetCategoryFragment.setOrderCategory(category);
        presetCategoryFragment.setListener(new PresetCategoryFragment.PresetOrderCategoryListener() {
            @Override
            public void onFinishSet(String custom_field1, String custom_field2, String custom_field3) {
                if (modelOrder == null)
                    modelOrder = new ModelOrder();
                modelOrder.setCustomfield_1(custom_field1);
                modelOrder.setCustomfield_2(custom_field2);
                modelOrder.setCustomfield_3(custom_field3);
                modelOrder.setOrder_category(category.getName());
                selectCustomer();

            }

        });
        presetCategoryFragment.show(fm, "Set Order Category");

    }

    private void selectCustomer(){
        FragmentManager fm = getFragmentManager();
        PickCustomerFragment pickCustomerFragment = new PickCustomerFragment();
        pickCustomerFragment.SetCustomerSelectListener(new PickCustomerFragment.CustomerSelectListener() {
            @Override
            public void OnSelectCustomer(ModelCustomer modelCustomer) {
                setCustomer(modelCustomer);
            }

            @Override
            public void OnClickCreateCustomer() {
                showDialogCreateCustomer();
            }
        });
        pickCustomerFragment.show(fm, "Pilih Customer");

//        loadOrder(null);
    }

    private void setCustomer(ModelCustomer modelCustomer){
        if (modelOrder == null)
            modelOrder = new ModelOrder();

        modelOrder.setCustomer(modelCustomer);
        loadOrder(modelOrder);
    }

    private void setCategory(String orderCategory){
        if (modelOrder == null)
            modelOrder = new ModelOrder();

        modelOrder.setOrder_category(orderCategory);

        //set preset
//        loadOrder(modelOrder);
    }

    private void showDialogCreateCustomer(){
        FragmentManager fm = getFragmentManager();
        CreateCustomerFragment createCustomerFragment = new CreateCustomerFragment();
        createCustomerFragment.SetOnCreatedListener(new CreateCustomerFragment.CustomerCreateListener() {
            @Override
            public void OnCreatedCustomer(ModelCustomer modelCustomer) {
                setCustomer(modelCustomer);
            }
        });
        createCustomerFragment.show(fm, "Buat Customer Baru");
    }

}
