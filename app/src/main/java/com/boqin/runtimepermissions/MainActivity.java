package com.boqin.runtimepermissions;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = (Button) findViewById(R.id.bt_permission);
        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = new ComponentName(v.getContext(), CameraActivity.class);
                try {
                    ActivityInfo activityInfo = getPackageManager().getActivityInfo(componentName, PackageManager.GET_META_DATA);
                    String permission = activityInfo.metaData.getString("permission");

                    PermissionActivity.starActivity(v.getContext(), permission, new PermissionActivity.PermissionResultListen() {
                        @Override
                        public void onGranted() {
                            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                            intent.putExtra("BQ","test");
                            MainActivity.this.startActivity(intent);
                        }

                        @Override
                        public void onDenied() {

                        }
                    });

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void showCamera() {
        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
        Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
    }

}
