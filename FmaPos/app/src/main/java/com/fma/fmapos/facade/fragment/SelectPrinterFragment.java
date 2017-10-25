package com.fma.fmapos.facade.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.CustomerListAdapter;
import com.fma.fmapos.adapter.PrinterListAdapter;
import com.fma.fmapos.controller.ControllerCustomer;
import com.fma.fmapos.facade.SettingActivity;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelPrinter;
import com.fma.fmapos.model.ModelSetting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by fma on 8/13/2017.
 */

public class SelectPrinterFragment extends DialogFragment implements PrinterListAdapter.ItemClickListener {
    List<ModelPrinter> printers;
    RecyclerView recyclerView;
    PrinterListAdapter adapter;
    BluetoothAdapter bluetoothAdapter;
    ModelSetting modelSetting;
    private SelectPrinterListener mSelectListener;

    public interface SelectPrinterListener{
        void onSelectPrinter(ModelSetting setting,ModelPrinter printer);
    }

    public void setSelectListener(SelectPrinterListener mSelectListener, ModelSetting modelSetting) {
        this.modelSetting = modelSetting;
        this.mSelectListener = mSelectListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_printer, container, false);
        getDialog().setTitle("Pilih Printer");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvDevices);

        printers = this.getPairedDevices();
        if (printers != null){
            adapter = new PrinterListAdapter(getActivity(), printers);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private List<ModelPrinter> getPairedDevices() {
        List<ModelPrinter> printers = new ArrayList<ModelPrinter>();
        String value;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try{
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0){

                for(BluetoothDevice device : pairedDevices){
                    printers.add(new ModelPrinter(device.getName(), device.getAddress()));
                }
            }
            else{
                value = "No Devices found";
                Toast.makeText(getActivity(), "No Devices found", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception ex){
            value = ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
        }
        return printers;
    }



    @Override
    public void onItemClick(View view, int position) {
        ModelPrinter printer = printers.get(position);
        if (mSelectListener != null) mSelectListener.onSelectPrinter(this.modelSetting, printer);
        dismiss();
    }



}
