package com.fma.fmapos.facade.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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
    CustomerSelectListener customerSelectListener;

//    public OrderFinishFragment parent;


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

//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
//        view.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
    }


    public interface CustomerSelectListener{
        void OnSelectCustomer(ModelCustomer modelCustomer);
    }

    public void SetCustomerSelectListener(CustomerSelectListener customerSelectListener){
        this.customerSelectListener = customerSelectListener;
    }

    @Override
    public void onItemClick(View view, int position) {
        ModelCustomer customer = customers.get(position);
        customerSelectListener.OnSelectCustomer(customer);
        dismiss();
    }
}
