package com.boqin.runtimepermissions;

import com.boqin.permissionapi.PermissionApiUtil;

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
//                ComponentName componentName = new ComponentName(v.getContext(), CameraActivity.class);
//                try {
//                    ActivityInfo activityInfo = getPackageManager().getActivityInfo(componentName, PackageManager.GET_META_DATA);
//                    String permission = activityInfo.metaData.doPermission("permission");
//
//                    PermissionActivity.starActivity(v.getContext(), permission, new PermissionActivity.PermissionResultListen() {
//                        @Override
//                        public void onGranted() {
//                            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                            intent.putExtra("BQ","test");
//                            MainActivity.this.startActivity(intent);
//                        }
//
//                        @Override
//                        public void onDenied() {
//
//                        }
//                    });
//
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                PermissionUtil.checkPermissionForIntent(MainActivity.this, CameraActivity.class, null);

                PermissionApiUtil.doPermission(MainActivity.this);
            }
        });
    }

    @BQAnnotation
    void showCamera() {
        Toast.makeText(this, "Annotation", Toast.LENGTH_SHORT).show();
    }

}
