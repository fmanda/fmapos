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
import android.widget.EditText;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.adapter.CategoryListAdapter;
import com.fma.kumo.controller.ControllerOrder;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelOrderCategory;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by fma on 8/13/2017.
 */

public class PresetCategoryFragment extends DialogFragment{
    Button btnNext;
    Button btnBack;
    EditText txtCustomField1;
    EditText txtCustomField2;
    EditText txtCustomField3;
    TextView lbCustomField1;
    TextView lbCustomField2;
    TextView lbCustomField3;
    PresetOrderCategoryListener presetOrderCategoryListener;
    private ModelOrderCategory modelOrderCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_ordercategory, container, false);
        getDialog().setTitle("Set Order Category");

        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnBack = (Button) view.findViewById(R.id.btnBack);

        txtCustomField1 = (EditText) view.findViewById(R.id.txtCustomField1);
        txtCustomField2 = (EditText) view.findViewById(R.id.txtCustomField2);
        txtCustomField3 = (EditText) view.findViewById(R.id.txtCustomField3);

        lbCustomField1 = (TextView) view.findViewById(R.id.lbCustomField1);
        lbCustomField2 = (TextView) view.findViewById(R.id.lbCustomField2);
        lbCustomField3 = (TextView) view.findViewById(R.id.lbCustomField3);

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
            lbCustomField1.setText(modelOrderCategory.getCustomfield_1());
            lbCustomField2.setText(modelOrderCategory.getCustomfield_2());
            lbCustomField3.setText(modelOrderCategory.getCustomfield_3());
//
            if (modelOrderCategory.getCustomfield_1().equals(""))
                txtCustomField1.setVisibility(View.GONE);
            if (modelOrderCategory.getCustomfield_2().equals(""))
                txtCustomField2.setVisibility(View.GONE);
            if (modelOrderCategory.getCustomfield_3().equals(""))
                txtCustomField3.setVisibility(View.GONE);

            lbCustomField1.setVisibility(txtCustomField1.getVisibility());
            lbCustomField2.setVisibility(txtCustomField2.getVisibility());
            lbCustomField3.setVisibility(txtCustomField3.getVisibility());

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
