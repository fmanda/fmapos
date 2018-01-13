package com.fma.kumo.facade.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.fma.kumo.R;
import com.fma.kumo.controller.ControllerSetting;
import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelReconcile;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by fma on 8/13/2017.
 */

public class ActualReconcileFragment extends DialogFragment {

    private ActReconcileDialogListener actReconcileDialogListener;

    EditText txtCash;
    EditText txtCard;
    Button btnSave;
    Button btnCancel;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actual_reconcile, container, false);
        getDialog().setTitle("Input Kas Masuk / Keluar");

        txtCash = (EditText) view.findViewById(R.id.txtCash);
        txtCard = (EditText) view.findViewById(R.id.txtCard);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        this.context = getActivity().getApplicationContext();

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveData();
                dismiss();
                actReconcileDialogListener.OnFinishDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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


    public interface ActReconcileDialogListener{
        void OnFinishDialog();
    }

    public void SetDialogListener(ActReconcileDialogListener actReconcileDialogListener){
        this.actReconcileDialogListener = actReconcileDialogListener;
    }


    public void saveData(){
        int company_id = new ControllerSetting(this.context).getCompanyID();
        int unit_id = new ControllerSetting(this.context).getUnitID();

        ModelReconcile modelReconcile = new ModelReconcile();

        //rekap sales, void, cash in, cash out
        modelReconcile.setCash_amount(Double.parseDouble(txtCash.getText().toString()));
        modelReconcile.setCard_amount(Double.parseDouble(txtCard.getText().toString()));

        modelReconcile.setCompany_id(company_id);
        modelReconcile.setUnit_id(unit_id);
        modelReconcile.setTransdate(new Date());

        DBHelper db = DBHelper.getInstance(this.context);

        modelReconcile.saveToDB(db.getWritableDatabase());
    }
}
