package com.boqin.runtimepermissions.util;

import com.boqin.runtimepermissions.PermissionActivity;
import com.boqin.runtimepermissions.fragment.PermissionFragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * TODO
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionUtil {

    public static void checkPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[] {permission},
                        0);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public static void checkPermissionForIntent(Activity activity, Class cl, PermissionActivity.PermissionResultListen permissionResultListen) {
        ComponentName componentName = new ComponentName(activity, cl);
        ActivityInfo activityInfo = null;
        try {
            activityInfo = activity.getPackageManager().getActivityInfo(componentName, PackageManager.GET_META_DATA);
            String permission = activityInfo.metaData.getString("permission");
            if (permission!=null) {

                FragmentManager fragmentManager = activity.getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("T");
                if (fragment == null) {
                    fragment = new PermissionFragment();
                    fragmentManager
                            .beginTransaction()
                            .add(fragment, "T")
                            .commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                }

//                PermissionActivity.starActivity(context, permission, new PermissionActivity.PermissionResultListen() {
//                    @Override
//                    public void onGranted() {
////                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
////                        intent.putExtra("BQ","test");
////                        MainActivity.this.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onDenied() {
//
//                    }
//                });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


}
