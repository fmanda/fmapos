package com.fma.fmapos.facade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.OrderItemListAdapter;
import com.fma.fmapos.facade.fragment.OrderFinishFragment;
import com.fma.fmapos.helper.CurrencyHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelOrder;

public class OrderFinishActivity extends AppCompatActivity {
    private ListView orderItemListView = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        };
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        Intent intent = getIntent();
        ModelOrder order = (ModelOrder) intent.getSerializableExtra("orderFromProductList");

        OrderFinishFragment orderFinishFragment = (OrderFinishFragment) getFragmentManager().findFragmentById(R.id.fragOrderFinish);
        orderFinishFragment.loadOrder(order);

//        orderItemListView = (ListView) findViewById(R.id.orderItemListView);
//        orderItemListView.setAdapter(new OrderItemListAdapter(this, order.getItems()));
//
//        TextView txtOrderFinishSubTotal = (TextView) findViewById(R.id.txtOrderFinishSubTotal);
//        TextView txtOrderFinishPPN = (TextView) findViewById(R.id.txtOrderFinishPPN);
//        TextView txtOrderFinishTotal = (TextView) findViewById(R.id.txtOrderFinishTotal);
//
//        txtOrderFinishSubTotal.setText(CurrencyHelper.format(order.getSubTotal()));
//        txtOrderFinishPPN.setText(CurrencyHelper.format(order.getTax()));
//        txtOrderFinishTotal.setText(CurrencyHelper.format(order.getSummary()));
    }

    private void refreshAdapter(){
        OrderItemListAdapter adapter = (OrderItemListAdapter) this.orderItemListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

//    public void onCustomerClick(View view){
//        ModelCustomer customer = (ModelCustomer) view.getTag();
//        Toast.makeText(this, customer.getName(), Toast.LENGTH_SHORT).show();
//    }
}
