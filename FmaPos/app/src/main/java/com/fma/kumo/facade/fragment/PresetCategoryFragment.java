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
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CategoryListAdapter;
import com.fma.kumo.controller.ControllerOrder;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelOrderCategory;

import java.util.List;

/**
 * Created by fma on 8/13/2017.
 */

public class PresetCategoryFragment extends DialogFragment{
    Button btnNext;
    Button btnBack;
    TextView txtCustomField1;
    TextView txtCustomField2;
    TextView txtCustomField3;
    PresetOrderCategoryListener presetOrderCategoryListener;
    private ModelOrderCategory modelOrderCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_ordercategory, container, false);
        getDialog().setTitle("Set Order Category");

        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnBack = (Button) view.findViewById(R.id.btnBack);

        txtCustomField1 = (TextView) view.findViewById(R.id.txtCustomField1);
        txtCustomField2 = (TextView) view.findViewById(R.id.txtCustomField2);
        txtCustomField3 = (TextView) view.findViewById(R.id.txtCustomField3);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetOrderCategoryListener.onFinishSet(txtCustomField1.getText().toString(),
                        txtCustomField2.getText().toString(),
                        txtCustomField3.getText().toString()
                );
                dismiss();
            }
        });

        if (modelOrderCategory != null) {
            txtCustomField1.setHint(modelOrderCategory.getCustomfield_1());
            txtCustomField2.setHint(modelOrderCategory.getCustomfield_2());
            txtCustomField3.setHint(modelOrderCategory.getCustomfield_3());

            if (modelOrderCategory.getCustomfield_1().equals(""))
                txtCustomField1.setVisibility(View.GONE);
            if (modelOrderCategory.getCustomfield_2().equals(""))
                txtCustomField2.setVisibility(View.GONE);
            if (modelOrderCategory.getCustomfield_3().equals(""))
                txtCustomField3.setVisibility(View.GONE);

            txtCustomField1.setText(null);
            txtCustomField2.setText(null);
            txtCustomField3.setText(null);
        }


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


    public interface PresetOrderCategoryListener{
        void onFinishSet(String custom_field1, String custom_field2, String custom_field3);
    }

    public void setListener(PresetOrderCategoryListener presetOrderCategoryListener){
        this.presetOrderCategoryListener = presetOrderCategoryListener;
    }

    public void setOrderCategory(ModelOrderCategory category){
        this.modelOrderCategory = category;
    }


}
