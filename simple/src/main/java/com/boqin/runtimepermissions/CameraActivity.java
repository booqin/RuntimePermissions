package com.boqin.runtimepermissions;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.boqin.permissionapi.RuntimePermission;

/**
 * 模拟照相权限
 * Created by Boqin on 2017/4/9.
 * Modified by Boqin
 *
 * @Version
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
