package com.fma.fmapos.facade;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.OrderItemListAdapter;
import com.fma.fmapos.facade.fragment.OrderFinishFragment;
import com.fma.fmapos.facade.fragment.PickCustomerFragment;
import com.fma.fmapos.helper.CurrencyHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelOrder;

public class OrderFinishActivity extends AppCompatActivity {
    private ListView orderItemListView = null;
    Button btnBack;
    Button btnLookupCustomer;
    Button btnDelCustomer;
    OrderFinishFragment orderFinishFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);

        Intent intent = getIntent();
        ModelOrder order = (ModelOrder) intent.getSerializableExtra("orderFromProductList");

        orderFinishFragment = (OrderFinishFragment) getFragmentManager().findFragmentById(R.id.fragOrderFinish);
        orderFinishFragment.loadOrder(order);

        btnLookupCustomer = (Button) findViewById(R.id.btnLookupCustomer);
        btnDelCustomer = (Button) findViewById(R.id.btnDelCustomer);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnLookupCustomer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialogCustomer();
            }
        });
        ModelCustomer modelCustomer = order.getCustomer();
        if (modelCustomer!=null){
            if (modelCustomer.getId()>0) btnLookupCustomer.setText(modelCustomer.getName());
        }

        btnDelCustomer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setCustomer(null);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void setCustomer(ModelCustomer modelCustomer){
        orderFinishFragment.modelOrder.setCustomer(modelCustomer);
        if (modelCustomer != null) {
            btnLookupCustomer.setText(modelCustomer.getName());
        }else{
            btnLookupCustomer.setText("  Pilih Customer");
        }
    }

    public void showDialogCustomer(){
        FragmentManager fm = getFragmentManager();
        PickCustomerFragment pickCustomerFragment = new PickCustomerFragment();
        pickCustomerFragment.SetCustomerSelectListener(new PickCustomerFragment.CustomerSelectListener() {
            @Override
            public void OnSelectCustomer(ModelCustomer modelCustomer) {
                setCustomer(modelCustomer);
            }
        });
        pickCustomerFragment.show(fm, "Pilih Customer");
    }

}
