package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fma.kumo.R;
import com.fma.kumo.facade.fragment.CashTransFragment;

/**
 * Created by fma on 7/30/2017.
 */

public class ReconcileActivity extends BaseActivity {
    Button btnCashTrans;

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
    }

    private void showDialogCashTrans(){
        FragmentManager fm = getFragmentManager();
        CashTransFragment cashTransFragment = new CashTransFragment();
        cashTransFragment.SetDialogListiner(new CashTransFragment.CashTransDialogListener() {
            @Override
            public void OnFinishDialog() {
                //do nothing
            }
        });
        cashTransFragment.show(fm, "Input Cash Trans");
    }

}

