package com.fma.kumo.facade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CustomerListAdapter;
import com.fma.kumo.controller.ControllerCustomer;
import com.fma.kumo.model.ModelCustomer;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class CustomerActivity extends BaseActivity implements CustomerListAdapter.ItemClickListener {

    private List<ModelCustomer> customers;
    private ControllerCustomer controllerCustomer = new ControllerCustomer(this);
    CustomerListAdapter customerListAdapter;
    RecyclerView recyclerView;
    SearchView txtSearchCustomer;

    public void fabOnClick(){
        Intent intent = new Intent(this, CustomerCreateActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer, this.mainframe);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateCustomer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabOnClick();
            }
        });

        controllerCustomer = new ControllerCustomer(this);
        customers = controllerCustomer.getCustomerList();
        customerListAdapter = new CustomerListAdapter(this, customers);
        txtSearchCustomer = (SearchView) this.findViewById(R.id.txtSearchCustomer);

        recyclerView = (RecyclerView) this.findViewById(R.id.rvCustomers);
        int numberOfColumns = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setAdapter(customerListAdapter);

        customerListAdapter.setClickListener(this);

        txtSearchCustomer = (SearchView) this.findViewById(R.id.txtSearchCustomer);
        txtSearchCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchCustomer(s);
                return false;
            }
        });
    }

    public void showEditor(ModelCustomer customer){
        Intent intent = new Intent(this, CustomerCreateActivity.class);
        intent.putExtra("customerFromList", customer);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        showEditor(customers.get(position));
    }

    private void searchCustomer(String customername){
        customers.clear();
        customers.addAll(
                controllerCustomer.getCustomerByFilter(customername)
        );
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}
