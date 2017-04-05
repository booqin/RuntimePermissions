package com.boqin.runtimepermissions;

import com.boqin.permissionapi.PermissionUtil;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@PermissionActivity({Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA})
public class MainActivity extends AppCompatActivity {

    Button mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtil.tryPermissionByAnnotation(this);
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
                PermissionUtil.tryPermissionByAnnotation(MainActivity.this);
            }

        });
    }

    @PermissionGranted(Manifest.permission.CAMERA)
    public void initCamera(){
        Toast.makeText(this, "CAMERA", Toast.LENGTH_SHORT).show();
    }

    @PermissionGranted(Manifest.permission.READ_CONTACTS)
    public void initContacts(){
        Toast.makeText(this, "READ_CONTACTS", Toast.LENGTH_SHORT).show();
    }
}
