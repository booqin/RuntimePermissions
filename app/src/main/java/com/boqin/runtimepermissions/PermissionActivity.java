package com.boqin.runtimepermissions;

import com.boqin.runtimepermissions.util.PermissionUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * TODO
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = PermissionActivity.class.getSimpleName();

    private String mPermission;

    private static PermissionResultListen mPListen;

    public static void starActivity(Context context, String permission, PermissionResultListen pl) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(TAG, permission);
        context.startActivity(intent);
        mPListen = pl;
    }

    @Override
    protected void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermission = getIntent().getStringExtra(TAG);
        PermissionUtil.checkPermission(this, mPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull
                    String[] permissions,
            @NonNull
                    int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            if (mPListen != null) {
                mPListen.onGranted();
            }

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            if (mPListen != null) {
                mPListen.onDenied();
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    public interface PermissionResultListen{
        void onGranted();
        void onDenied();
    }
}
