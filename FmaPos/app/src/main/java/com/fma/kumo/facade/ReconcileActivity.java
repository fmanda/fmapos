package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CashTransAdapter;
import com.fma.kumo.adapter.ReconcileAdapter;
import com.fma.kumo.controller.ControllerReconcile;
import com.fma.kumo.facade.fragment.CashTransFragment;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelReconcile;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class ReconcileActivity extends BaseActivity {
    ReconcileAdapter reconcileAdapter;
    RecyclerView rvReconcile;
    private List<ModelReconcile> reconcileList;
    private ControllerReconcile controllerReconcile = new ControllerReconcile(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_reconcile, this.mainframe);

        rvReconcile = (RecyclerView) this.findViewById(R.id.rvReconcile);
        int numberOfColumns = 1;
        rvReconcile.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        reconcileList = controllerReconcile.getReconcileList();
        reconcileAdapter = new ReconcileAdapter(this, reconcileList);
        rvReconcile.setAdapter(reconcileAdapter);

    }

    public void refreshData(){
        reconcileList.clear();
        reconcileList.addAll(
                controllerReconcile.getReconcileList()
        );
        rvReconcile.getAdapter().notifyDataSetChanged();
    }

}

