package com.fma.fmapos.facade.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.fma.fmapos.R;
import com.fma.fmapos.adapter.CustomerListAdapter;
import com.fma.fmapos.controller.ControllerCustomer;
import com.fma.fmapos.model.ModelCustomer;

import java.util.List;

/**
 * Created by fma on 8/13/2017.
 */

public class PickCustomerFragment extends DialogFragment implements CustomerListAdapter.ItemClickListener {
    List<ModelCustomer> customers;
    ControllerCustomer controllerCustomer;
    CustomerListAdapter customerListAdapter;
    RecyclerView recyclerView;

    public OrderFinishFragment parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_customer, container, false);
        getDialog().setTitle("Pilih Customer");

        controllerCustomer = new ControllerCustomer(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCustomers);
        int numberOfColumns = 1;
        customers = controllerCustomer.getCustomerList();
        customerListAdapter = new CustomerListAdapter(getActivity(), customers);
        recyclerView.setAdapter(customerListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        customerListAdapter.setClickListener(this);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }



    public void prepare(OrderFinishFragment parent){
        this.parent = parent;
    }

    public void selectCustomer(ModelCustomer customer){
        parent.setCustomer(customer);
    }


    @Override
    public void onItemClick(View view, int position) {
        ModelCustomer customer = customers.get(position);
        selectCustomer(customer);
        dismiss();
    }
}
