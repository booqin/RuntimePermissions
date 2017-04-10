package com.boqin.runtimepermissions;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.boqin.permissionapi.RuntimePermission;

/**
 * Desctiption: TODO
 * Created by Vito on 2017/4/9.
 * Email:developervito@163.com
 * ModifiedBy: Vito
 * ModifiedTime: 2017/4/9 15:59
 * ModifiedNotes: TODO
 * Version 1.0
 */
@PermissionActivity(Manifest.permission.CAMERA)
public class CameraActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findViewById(R.id.bt_camera).setOnClickListener(this);

    }

    @PermissionGranted(Manifest.permission.CAMERA)
    public void starCamera(){
        Toast.makeText(this, "开始录像", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_camera) {
            RuntimePermission.tryPermissionByAnnotation(this);
        }
    }
}
