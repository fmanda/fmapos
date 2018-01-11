package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CashTransAdapter;
import com.fma.kumo.controller.ControllerReconcile;
import com.fma.kumo.facade.fragment.CashTransFragment;
import com.fma.kumo.model.ModelCashTrans;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class ReconcileActivity extends BaseActivity {
    Button btnCashTrans;
    CashTransAdapter cashTransAdapter;
    RecyclerView rvCashTrans;
    private List<ModelCashTrans> cashTrans;
    private ControllerReconcile controllerReconcile = new ControllerReconcile(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_reconcile, this.mainframe);
        btnCashTrans = (Button) findViewById(R.id.btnCashTrans);
        btnCashTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCashTrans();
            }
        });
        rvCashTrans = (RecyclerView) this.findViewById(R.id.rvCashTrans);
        int numberOfColumns = 1;
        rvCashTrans.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        cashTrans = controllerReconcile.getCashTransList(0);
        cashTransAdapter = new CashTransAdapter(this, cashTrans);
        rvCashTrans.setAdapter(cashTransAdapter);

    }

    private void showDialogCashTrans(){
        FragmentManager fm = getFragmentManager();
        CashTransFragment cashTransFragment = new CashTransFragment();
        cashTransFragment.SetDialogListiner(new CashTransFragment.CashTransDialogListener() {
            @Override
            public void OnFinishDialog() {
                refreshData();
            }
        });
        cashTransFragment.show(fm, "Input Cash Trans");
    }

    public void refreshData(){
        cashTrans.clear();
        cashTrans.addAll(
                controllerReconcile.getCashTransList(0)
        );
        rvCashTrans.getAdapter().notifyDataSetChanged();
    }

}

