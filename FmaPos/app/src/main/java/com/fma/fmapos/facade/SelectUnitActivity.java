package com.fma.fmapos.facade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fma.fmapos.R;
import com.fma.fmapos.adapter.SelectUnitAdapter;
import com.fma.fmapos.controller.ControllerRequest;
import com.fma.fmapos.controller.ControllerRest;
import com.fma.fmapos.controller.ControllerSetting;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.helper.GsonRequest;
import com.fma.fmapos.model.ModelCompany;
import com.fma.fmapos.model.ModelProduct;
import com.fma.fmapos.model.ModelSetting;
import com.fma.fmapos.model.ModelUnit;

import java.util.Arrays;
import java.util.List;

public class SelectUnitActivity extends AppCompatActivity {
    SelectUnitAdapter selectUnitAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_unit);

        String url = ControllerRest.getURLUnits() + "/" + new ControllerSetting(this).getCompanyID();

        GsonRequest<ModelUnit[]> gsonReq = new GsonRequest<ModelUnit[]>(url, ModelUnit[].class,
            new Response.Listener<ModelUnit[]>() {
                @Override
                public void onResponse(ModelUnit[] response) {
                    try {
                        loadRecyclerView(response);
                    }catch(Exception e){
                        Toast.makeText(SelectUnitActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SelectUnitActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );
        ControllerRequest.getInstance(this).addToRequestQueue(gsonReq,url);




    }

    private void loadRecyclerView(ModelUnit[] response) {
        selectUnitAdapter = new SelectUnitAdapter(this, Arrays.asList(response));
        recyclerView = (RecyclerView) this.findViewById(R.id.rvSelectUnit);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(selectUnitAdapter);
        selectUnitAdapter.setClickListener(new SelectUnitAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ModelUnit modelUnit) {
                saveUnits(modelUnit);
            }
        });
    }

    private void saveUnits(ModelUnit modelUnit){
        ControllerSetting controllerSetting = new ControllerSetting(this);
        controllerSetting.updateSetting( "unit_id",Integer.toString(modelUnit.getId()) );
        controllerSetting.updateSetting( "unit_name",modelUnit.getName() );
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
