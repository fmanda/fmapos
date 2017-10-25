package com.fma.fmapos.facade;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fma.fmapos.R;
import com.fma.fmapos.helper.PrinterHelper;
import com.fma.fmapos.model.ModelProduct;

/**
 * Created by fma on 7/30/2017.
 */

public class MainActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, this.mainframe);
        startActivity(new Intent(this, RestActivity.class));

//
    }

}

