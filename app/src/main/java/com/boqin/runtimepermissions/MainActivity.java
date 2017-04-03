package com.boqin.runtimepermissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.boqin.permissionapi.PermissionUtil;

@BQAnnotation({Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA})
public class MainActivity extends AppCompatActivity {

    Button mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtil.tryPermission(this, null);
        mPermission = (Button) findViewById(R.id.bt_permission);

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PermissionApiUtil.doPermissionByAnnotation(MainActivity.this);
//                PermissionUtil.tryPermission(MainActivity.this, new PermissionFragment.PermissionsResultListenter() {
//                    @Override
//                    public void onGranted(String permission) {
//                        if (permission == Manifest.permission.READ_CONTACTS) {
//
//                        }else if (permission == Manifest.permission.CAMERA){
//
//                        }
//                        Log.d("BQ", permission);
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
//                        if (permission == Manifest.permission.READ_CONTACTS) {
//                            Log.d("BQ", permission);
//                        }
//                    }
//                });
            }

        });
    }

}
