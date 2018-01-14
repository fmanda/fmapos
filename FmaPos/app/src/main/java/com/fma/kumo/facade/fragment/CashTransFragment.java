package com.fma.kumo.facade.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CustomerListAdapter;
import com.fma.kumo.controller.ControllerCustomer;
import com.fma.kumo.controller.ControllerSetting;
import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelCustomer;

import java.util.Date;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by fma on 8/13/2017.
 */

public class CashTransFragment extends DialogFragment {

    private CashTransDialogListener cashTransDialogListener;

    EditText txtAmount;
    EditText txtNotes;
    RadioButton rbCashIn;
    RadioButton rbCashOut;
    Button btnSave;
    Button btnCancel;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_trans, container, false);
        getDialog().setTitle("Input Kas Masuk / Keluar");

        txtAmount = (EditText) view.findViewById(R.id.txtAmount);
        txtNotes = (EditText) view.findViewById(R.id.txtNotes);
        rbCashIn = (RadioButton) view.findViewById(R.id.rbCashIn);
        rbCashOut = (RadioButton) view.findViewById(R.id.rbCashOut);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        rbCashIn.setChecked(TRUE);
        rbCashOut.setChecked(FALSE);

        this.context = getActivity().getApplicationContext();

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveData();
                dismiss();
                cashTransDialogListener.OnFinishDialog();
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


    public interface CashTransDialogListener{
        void OnFinishDialog();
    }

    public void SetDialogListiner(CashTransDialogListener cashTransDialogListener){
        this.cashTransDialogListener = cashTransDialogListener;
    }


    public void saveData(){
        int company_id = new ControllerSetting(this.context).getCompanyID();
        int unit_id = new ControllerSetting(this.context).getUnitID();

        ModelCashTrans modelCashTrans = new ModelCashTrans();
        if (rbCashIn.isChecked()){
            modelCashTrans.setAmount(Double.parseDouble((txtAmount.getText().toString())));
        }else{
            modelCashTrans.setAmount(-1 * Double.parseDouble((txtAmount.getText().toString())));
        }
        modelCashTrans.setNotes(txtNotes.getText().toString());
        modelCashTrans.setReconcile_id(0);
        modelCashTrans.setCompany_id(company_id);
        modelCashTrans.setUnit_id(unit_id);
        modelCashTrans.setTransdate(new Date());

        DBHelper db = DBHelper.getInstance(this.context);

        modelCashTrans.setUploaded(0);
        modelCashTrans.saveToDB(db.getWritableDatabase());


    }
}
