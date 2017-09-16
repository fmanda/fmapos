package com.fma.fmapos.facade;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.controller.ControllerSetting;
import com.fma.fmapos.facade.fragment.PickCustomerFragment;
import com.fma.fmapos.facade.fragment.SelectPrinterFragment;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.model.ModelSetting;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class SettingActivity extends BaseActivity {
    TextView txtCashierPrinter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_setting, this.mainframe);
        txtCashierPrinter = (TextView) findViewById(R.id.txtCashierPrinter);
        loadData();
    }

    public void onBtnResetDBClick(View v){
        DBHelper db = new DBHelper(this);
        db.resetDatabase(db.getWritableDatabase());
        Toast.makeText(this, "Local DB reset", Toast.LENGTH_SHORT).show();
    }

    public void onBtnDummyDataClick(View v){
        DBHelper db = new DBHelper(this);
        db.dummyProduct(db.getWritableDatabase());
        Toast.makeText(this, "Dummy Data Pump", Toast.LENGTH_SHORT).show();
    }

    public void onChasierPrinterSelect(View v){
        FragmentManager fm = getFragmentManager();
        SelectPrinterFragment selectPrinter = new SelectPrinterFragment();
        selectPrinter.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        selectPrinter.setVarName("cashier_printer");
        selectPrinter.setParent(this);
        selectPrinter.show(fm, "Pilih Printer");
    }

    public void loadData(){
        try {
            ControllerSetting controllerSetting = new ControllerSetting(this);
            List<ModelSetting> settings = controllerSetting.getSettings();
            for (ModelSetting setting : settings) {
                if (setting.getVarname().equals("cashier_printer")) {
                    if (!setting.getVarvalue().equals(""))  txtCashierPrinter.setText(setting.getVarvalue());
                }
            }
        }catch(Exception ex){
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}

