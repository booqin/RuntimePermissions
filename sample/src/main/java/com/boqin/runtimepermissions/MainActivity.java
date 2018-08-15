package com.boqin.runtimepermissions;

import com.boqin.permissionapi.RuntimePermission;
import com.boqin.permissionapi.fragment.PermissionFragment;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Demo
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */
@PermissionActivity({Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION})
public class MainActivity extends AppCompatActivity {

    private Button mPermission;
    private Button mSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = findViewById(R.id.bt_permission);
        mSet = findViewById(R.id.bt_set);

        RuntimePermission.tryPermissionByAnnotation(MainActivity.this, true);

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                MainActivity.this.startActivity(localIntent);
            }
        });
    }

    @PermissionGranted
    public void initCamera(){
        Toast.makeText(this, "READ_CONTACTS is granted", Toast.LENGTH_SHORT).show();
    }
}
