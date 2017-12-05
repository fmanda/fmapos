package com.fma.kumo.facade.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CategoryListAdapter;
import com.fma.kumo.adapter.CustomerListAdapter;
import com.fma.kumo.controller.ControllerCustomer;
import com.fma.kumo.controller.ControllerOrder;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrderCategory;

import java.util.List;

/**
 * Created by fma on 8/13/2017.
 */

public class PickCategoryFragment extends DialogFragment implements CategoryListAdapter.ItemClickListener {
    List<ModelOrderCategory> categories;
    ControllerOrder controllerOrder;
    CategoryListAdapter categoryListAdapter;
    RecyclerView recyclerView;
    CategorySelectListener categorySelectListener;

//    public OrderFinishFragment parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_ordercategory, container, false);
        getDialog().setTitle("Pilih Order Category");

        controllerOrder = new ControllerOrder(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCategories);
        int numberOfColumns = 1;
        categories = controllerOrder.getOrderCategory();
        categoryListAdapter = new CategoryListAdapter(getActivity(), categories);
        recyclerView.setAdapter(categoryListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        categoryListAdapter.setClickListener(this);


        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
//        view.setLayoutParams(layoutParams);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
    }


    public interface CategorySelectListener{
        void OnSelectCategory(ModelOrderCategory modelOrderCategory);
    }

    public void SetCategorySelectListener(CategorySelectListener categorySelectListener){
        this.categorySelectListener = categorySelectListener;
    }



    @Override
    public void onItemClick(View view, int position) {
        ModelOrderCategory modelOrderCategory = categories.get(position);
        categorySelectListener.OnSelectCategory(modelOrderCategory);
        dismiss();
    }


}
