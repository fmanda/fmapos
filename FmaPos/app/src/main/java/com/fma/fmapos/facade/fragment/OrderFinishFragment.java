package com.fma.fmapos.facade.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.OrderItemListAdapter;
import com.fma.fmapos.facade.MainActivity;
import com.fma.fmapos.facade.OrderActivity;
import com.fma.fmapos.facade.PaymentActivity;
import com.fma.fmapos.helper.CurrencyHelper;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.helper.OrderPrinterHelper;
import com.fma.fmapos.helper.PrinterHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelOrder;

/**
 * Created by fmanda on 08/10/17.
 */

public class OrderFinishFragment extends Fragment{

    TextView txtOrderFinishSubTotal;
    TextView txtOrderFinishPPN;
    TextView txtOrderFinishTotal;
    TextView txtOrderNo;

//    Button btnSaveOrder;
    Button btnPayOrder;
    Button btnHoldOrder;

    public ModelOrder modelOrder;

    RecyclerView recyclerView;
    OrderItemListAdapter orderItemListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_finish, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvOrderFinish);


        txtOrderFinishSubTotal = (TextView) view.findViewById(R.id.txtOrderFinishSubTotal);
        txtOrderFinishPPN = (TextView) view.findViewById(R.id.txtOrderFinishPPN);
        txtOrderFinishTotal = (TextView) view.findViewById(R.id.txtOrderFinishTotal);
        txtOrderNo = (TextView) view.findViewById(R.id.txtOrderNo);


//        btnSaveOrder = (Button) view.findViewById(R.id.btnSaveOrder);
        btnPayOrder = (Button) view.findViewById(R.id.btnPayOrder);
        btnHoldOrder = (Button) view.findViewById(R.id.btnHoldOrder);


//        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                modelOrder.setStatus(1); //print to kitchen
//                saveData();
//            }
//        });

        btnHoldOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelOrder.setStatus(0); //without print
                saveData();
            }
        });

        btnPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payOrder();
            }
        });

        return view;
    }

    private void payOrder() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("modelOrder", modelOrder);
        startActivity(intent);

    }

    public void loadOrder(ModelOrder modelOrder){
        this.modelOrder = modelOrder;
        orderItemListAdapter = new OrderItemListAdapter(getActivity(), modelOrder.getItems());

        recyclerView.setAdapter(orderItemListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        txtOrderFinishSubTotal.setText(CurrencyHelper.format(modelOrder.getSubTotal()));
        txtOrderFinishPPN.setText(CurrencyHelper.format(modelOrder.getTax()));
        txtOrderFinishTotal.setText(CurrencyHelper.format(modelOrder.getSummary()));
        txtOrderNo.setText("#" + modelOrder.getOrderno());
    }

    private void saveData(){
        DBHelper db = DBHelper.getInstance(getActivity());
        SQLiteDatabase trans = db.getWritableDatabase();
        modelOrder.saveToDBAll(trans);
//        OrderPrinterHelper printer = new OrderPrinterHelper(getActivity());
//        printer.PrintOrder(modelOrder);

        startActivity(new Intent(getActivity(), OrderActivity.class));
    }

}
