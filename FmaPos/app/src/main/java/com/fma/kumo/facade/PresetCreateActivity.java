package com.fma.kumo.facade;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fma.kumo.R;
import com.fma.kumo.controller.ControllerCustomer;
import com.fma.kumo.facade.fragment.PickCustomerFragment;
import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrderPreset;


/**
 * Created by fma on 7/30/2017.
 */


public class PresetCreateActivity extends AppCompatActivity {
    ModelOrderPreset modelOrderPreset = new ModelOrderPreset();
    EditText txtOrderPreset;
    TextView txtCustName;
    Button btnSetCust;
    Button btnRemoveCust;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, 0, 0, "Delete").setIcon(R.drawable.ic_menu_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0, 0, 0, "Done").setIcon(R.drawable.ic_menu_checkall)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }else if (item.getTitle().equals("Done")){
            this.savePreset();
        }else if (item.getTitle().equals("Delete")){
            this.deletePreset();
        }
        return super.onOptionsItemSelected(item);

    }

    private void savePreset() {
        try {
            modelOrderPreset.setName(txtOrderPreset.getText().toString());
        }catch (Exception e){
            Log.d("exception", e.getMessage());
        }

        DBHelper db = DBHelper.getInstance(this);
        SQLiteDatabase trans = db.getWritableDatabase();
        modelOrderPreset.saveToDB(trans, true);

        Toast.makeText(this, "Preset berhasil diupdate", Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this, PresetActivity.class);
        startActivity(intent);

    }

    private void deletePreset() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Anda yakin menghapus data Preset ini?");
        builder.setPositiveButton("Confirm",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doDeletePreset();
                }
            });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void doDeletePreset(){
        if (modelOrderPreset == null) return;
        int id = modelOrderPreset.getId();
        if (id == 0) return;

        DBHelper db = DBHelper.getInstance(this);
        SQLiteDatabase trans = db.getWritableDatabase();
        modelOrderPreset.deleteFromDB(trans);

        Toast.makeText(this, "Preset berhasil dihapus", Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this, PresetActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(v.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_preset);

        txtCustName = (TextView) findViewById(R.id.txtCustName);
        txtOrderPreset = (EditText) findViewById(R.id.txtOrderPreset);
        btnSetCust = (Button) findViewById(R.id.btnSetCust);
        btnRemoveCust = (Button) findViewById(R.id.btnRemoveCust);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setTitle("Buat Preset Baru");
        }

        loadData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void loadData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("modelOrderPreset")) {
                modelOrderPreset = (ModelOrderPreset) intent.getSerializableExtra("modelOrderPreset");
                getSupportActionBar().setTitle("Ubah Preset");
            }
        }

        if (modelOrderPreset.getDefault_customer_id()>0){
            ControllerCustomer controllerCustomer= new ControllerCustomer(this);
            ModelCustomer modelCustomer = controllerCustomer.getCustomer(modelOrderPreset.getDefault_customer_id());
            if (modelCustomer != null) txtCustName.setText(modelCustomer.getName());

        }
        txtOrderPreset.setText(modelOrderPreset.getName());
    }



}
