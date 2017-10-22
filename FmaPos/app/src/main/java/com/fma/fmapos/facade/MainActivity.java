package com.fma.fmapos.facade;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fma.fmapos.R;
import com.fma.fmapos.helper.PrinterHelper;

/**
 * Created by fma on 7/30/2017.
 */

public class MainActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, this.mainframe);
//        DBHelper db = new DBHelper(this);
//        SQLiteDatabase trans = db.getWritableDatabase();
//        db.dummyProduct(trans);

        startActivity(new Intent(this, RestActivity.class));
        final PrinterHelper printerHelper = new PrinterHelper(this);
        Button btnPrint = (Button) findViewById(R.id.btnPrint);
        final EditText edTestPrint = (EditText) findViewById(R.id.edTestPrint);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printerHelper.TestPrintData((edTestPrint.getText().toString() + "\n").getBytes());
            }
        });


//
    }

}

