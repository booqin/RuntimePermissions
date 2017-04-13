package com.boqin.runtimepermissions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boqin.permissionapi.RuntimePermission;

/**
 * Demo
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */
@PermissionActivity(Manifest.permission.READ_CONTACTS)
public class MainActivity extends AppCompatActivity {

    private Button mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermission = (Button) findViewById(R.id.bt_permission);

        RuntimePermission.tryPermissionByAnnotation(MainActivity.this, false);

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @PermissionGranted(Manifest.permission.READ_CONTACTS)
    public void initCamera(){
        Toast.makeText(this, "READ_CONTACTS is granted", Toast.LENGTH_SHORT).show();
    }
}
