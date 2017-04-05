package com.boqin.runtimepermissions;

import com.boqin.permissionapi.RuntimePermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

@PermissionActivity({Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA})
public class MainActivity extends AppCompatActivity {

    private Button mPermission;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermission = (Button) findViewById(R.id.bt_permission);
        mTextView = (TextView) findViewById(R.id.text);
        RuntimePermission.tryPermissionByAnnotation(this);

    }

    @PermissionGranted(Manifest.permission.CAMERA)
    public void initCamera(){
        mTextView.setText("camera is granted");
    }
}
