package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        reconcileAdapter.setClickListener(new ReconcileAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    createReconcile();
                }else{
                    loadReconcile(reconcileList.get(position-1));
                }
            }
        });

    }

    private void createReconcile(){
        startActivity(new Intent(this, ReconcileCreateActivity.class));
    }

    private void loadReconcile(ModelReconcile modelReconcile){
        Toast.makeText(this, modelReconcile.getNotes(), Toast.LENGTH_SHORT).show();
    }

//    public void refreshData(){
//        reconcileList.clear();
//        reconcileList.addAll(
//                controllerReconcile.getReconcileList()
//        );
//        rvReconcile.getAdapter().notifyDataSetChanged();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Restarted", Toast.LENGTH_SHORT).show();
    }
}

