package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CashTransAdapter;
import com.fma.kumo.controller.ControllerReconcile;
import com.fma.kumo.controller.ControllerSetting;
import com.fma.kumo.facade.fragment.ActualReconcileFragment;
import com.fma.kumo.facade.fragment.CashTransFragment;
import com.fma.kumo.helper.CurrencyHelper;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelReconcile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fma on 7/30/2017.
 */

public class ReconcileDetailActivity extends AppCompatActivity {

    TextView txtSales;
    TextView txtVoid;
    TextView txtCashIn;
    TextView txtCashOut;
    TextView txtCash;
    TextView txtCard;
    TextView txtSysIncome;
    TextView txtActIncome;
    TextView txtVariant;
    TextView txtHeader;
    TextView txtSubHeader;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reconcile);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setTitle("Detail Rekap Kas");

        }

        txtSales = (TextView) findViewById(R.id.txtSales);
        txtVoid = (TextView) findViewById(R.id.txtVoid);
        txtCashIn = (TextView) findViewById(R.id.txtCashIn);
        txtCashOut = (TextView) findViewById(R.id.txtCashOut);
        txtCash = (TextView) findViewById(R.id.txtCash);
        txtCard = (TextView) findViewById(R.id.txtCard);
        txtSysIncome = (TextView) findViewById(R.id.txtSysIncome);
        txtActIncome = (TextView) findViewById(R.id.txtActIncome);
        txtVariant = (TextView) findViewById(R.id.txtVariant);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtSubHeader = (TextView) findViewById(R.id.txtSubHeader);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("modelReconcile")) {
                ModelReconcile modelReconcile = (ModelReconcile) intent.getSerializableExtra("modelReconcile");
                loadData(modelReconcile);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, ReconcileActivity.class));
        finish();

    }


    public void loadData(ModelReconcile modelReconcile){
        txtSales.setText(CurrencyHelper.format(modelReconcile.getSales_amount()));
        txtVoid.setText(CurrencyHelper.format(modelReconcile.getVoid_amount()));
        txtCashIn.setText(CurrencyHelper.format(modelReconcile.getCash_in()));
        txtCashOut.setText(CurrencyHelper.format(modelReconcile.getCash_out()));
        txtCash.setText(CurrencyHelper.format(modelReconcile.getCash_amount()));
        txtCard.setText(CurrencyHelper.format(modelReconcile.getCard_amount()));
        txtSysIncome.setText(CurrencyHelper.format(modelReconcile.getSysIncome()));
        txtActIncome.setText(CurrencyHelper.format(modelReconcile.getActIncome()));
        txtVariant.setText(CurrencyHelper.format(modelReconcile.getVariant()));

        txtHeader.setText(new ControllerSetting(this).getUnitName());
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yy H:mm", new Locale("id", "ID"));
        txtSubHeader.setText(dtf.format(modelReconcile.getTransdate()));
    }


}

