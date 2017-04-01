package com.boqin.runtimepermissions;

import com.boqin.permissionapi.PermissionApiUtil;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = (Button) findViewById(R.id.bt_permission);

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionApiUtil.doPermission(MainActivity.this);
            }
        });
    }

    @BQAnnotation(Manifest.permission.READ_CONTACTS)
    void showCamera() {
        Toast.makeText(this, "showCamera", Toast.LENGTH_SHORT).show();
    }
}
