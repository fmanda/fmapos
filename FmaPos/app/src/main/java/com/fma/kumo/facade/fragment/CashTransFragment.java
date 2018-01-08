package com.fma.kumo.facade.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CustomerListAdapter;
import com.fma.kumo.controller.ControllerCustomer;
import com.fma.kumo.model.ModelCustomer;

import java.util.List;

/**
 * Created by fma on 8/13/2017.
 */

public class CashTransFragment extends DialogFragment {

    private CashTransDialogListener cashTransDialogListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_trans, container, false);
        getDialog().setTitle("Input Kas Masuk / Keluar");


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


    public interface CashTransDialogListener{
        void OnFinishDialog();
    }

    public void SetDialogListiner(CashTransDialogListener cashTransDialogListener){
        this.cashTransDialogListener = cashTransDialogListener;
    }



}
