package com.fma.fmapos.facade;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.adapter.CategoryListAdapter;
import com.fma.fmapos.controller.ControllerProduct;
import com.fma.fmapos.facade.fragment.OrderFinishFragment;
import com.fma.fmapos.facade.fragment.PickProductFragment;
import com.fma.fmapos.helper.CurrencyHelper;

import java.util.List;


/**
 * Created by fma on 7/30/2017.
 */


public class OrderCreateActivity extends AppCompatActivity implements CategoryListAdapter.ItemClickListener {
    PickProductFragment pickProductFragment;
    OrderFinishFragment orderFinishFragment;
    EditText txtSearchProductList;
    Spinner spCategory;
    Switch swFilter;
    LinearLayout pnlCreateOrderSearch;
    ControllerProduct controllerProduct;
    RecyclerView rvCategory;
    public String categoryFilter = "";
    List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        pnlCreateOrderSearch = (LinearLayout) findViewById(R.id.pnlCreateOrderSearch);
        orderFinishFragment = (OrderFinishFragment) getFragmentManager().findFragmentById(R.id.fragOrderFinish);
        pickProductFragment = (PickProductFragment) getFragmentManager().findFragmentById(R.id.fragPickProduct);

        pickProductFragment.setParent(this);
        pickProductFragment.setMode(isTablet());

        if (getSupportActionBar() != null){
            if (isTablet()){
                getSupportActionBar().hide();
            }else{
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);

                getSupportActionBar().setDisplayShowCustomEnabled(true);
                getSupportActionBar().setTitle("Buat Order Baru");
            }
        }

//        spCategory = (Spinner) findViewById(R.id.spCategory);

        txtSearchProductList = (EditText) findViewById(R.id.txtSearchProductList);
        txtSearchProductList.clearFocus();
        txtSearchProductList.addTextChangedListener(new TextWatcher() {
            Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
            Runnable workRunnable;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(workRunnable);
                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        searchProduct();
                    }
                };
                handler.postDelayed(workRunnable, 500 /*delay*/);
            }
        });

        //test category
        controllerProduct = new ControllerProduct(this);
        categories = controllerProduct.getListCategory();
        spCategory = (Spinner) findViewById(R.id.spCategory);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spCategory.setAdapter(adapter);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryFilter = "";
                if (position>0) categoryFilter = categories.get(position);
                searchProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //end

        swFilter = (Switch) findViewById(R.id.swFilter);
        swFilter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showOrHideSearch();
            }
        });

        showOrHideSearch();


        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        if (rvCategory != null){
            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(this,
                    controllerProduct.getListCategory());
            rvCategory.setAdapter(categoryListAdapter);
            rvCategory.setLayoutManager(new GridLayoutManager(this, 1));
            categoryListAdapter.setClickListener(this);
        }


    }

    private void showOrHideSearch() {
        if (swFilter.isChecked()){
            pnlCreateOrderSearch.setVisibility(View.VISIBLE);
        }else{
            pnlCreateOrderSearch.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Done").setIcon(R.drawable.ic_menu_checkall)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            return backToBrowseOrder();
        }else if (item.getTitle() == "Done"){
            this.onBtnProductListSave(null);
        }
        return super.onOptionsItemSelected(item);

    }

    public boolean backToBrowseOrder(){
        this.finish();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(v.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    public void onBtnProductListSave(View view){
        Intent intent;
        if (!isTablet()) {
            intent = new Intent(this, OrderFinishActivity.class);
            intent.putExtra("orderFromProductList", pickProductFragment.orders);
            startActivity(intent);
        }else{
            Toast.makeText(this, "make order tablet here", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isTablet(){
        Boolean tabletMode = false;
        if (orderFinishFragment != null) {
            tabletMode = orderFinishFragment.isInLayout();
        }
        return tabletMode;
    }

    public void summaryOrder(){
        if (isTablet()){
            orderFinishFragment.loadOrder(pickProductFragment.orders);
        }

        TextView txtSummaryProductList = (TextView) findViewById(R.id.txtSummaryProductList);
        TextView txtCountProductList = (TextView) findViewById(R.id.txtCountProductList);
        txtSummaryProductList.setText("Total: " + CurrencyHelper.format(pickProductFragment.orders.getSummary()) );
        txtCountProductList.setText("Items: " + String.valueOf(pickProductFragment.orders.itemCount()) );
    }

    private void searchProduct(){
        pickProductFragment.products.clear();
        pickProductFragment.products.addAll(
                pickProductFragment.controllerProduct.getProductListByFilter(
                        categoryFilter,  txtSearchProductList.getText().toString()) );
        pickProductFragment.restoreProductSelection(pickProductFragment.products);
        pickProductFragment.refreshAdapter();
    }


    @Override
    public void onItemClick(View view, int position) {
        categoryFilter = "";
        if (position>0) categoryFilter = categories.get(position);
        searchProduct();
    }
}
