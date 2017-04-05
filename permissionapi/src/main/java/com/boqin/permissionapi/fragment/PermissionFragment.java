package com.boqin.permissionapi.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限相关的Fragment，有对应的生命周期，同时有获取权限请求结果的回调方法
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionFragment extends Fragment {

    private List<String> mGrantedList;
    private List<String> mDeniedList;

    /** 权限请求结果回调接口 */
    private PermissionsResultListenter mPermissionsResultListenter;

    @Override
    public void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mGrantedList = new ArrayList<>();
        mDeniedList = new ArrayList<>();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(@NonNull String[] permissions) {
        mGrantedList.clear();
        mDeniedList.clear();

        for (String permission : permissions) {
            initDeniedPermissionString(getActivity(), permission);
        }

        String[] strings = new String[mDeniedList.size()];
        mDeniedList.toArray(strings);
        if (strings.length==0) {
            return;
        }
        requestPermissions(strings, 0);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,
            @NonNull
                    String[] permissions,
            @NonNull
                    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            switch (grantResults[i]){
                case PackageManager.PERMISSION_GRANTED:
                    mDeniedList.remove(grantResults[i]);
                    if(mPermissionsResultListenter!=null){
                        mPermissionsResultListenter.onGranted(permissions[i]);
                    }
                    break;
                case PackageManager.PERMISSION_DENIED:
                    if(mPermissionsResultListenter!=null){
                        mPermissionsResultListenter.onDenied(permissions[i]);
                    }
                    break;
                default:
                    break;
            }
        }
        if(mDeniedList.size()!=0){
            showRationaleDialog(mDeniedList.toString());
        }
    }

    /**
     * 设置对应接口
     * @param permissionsResultListenter
     */
    public void setPermissionsResultListenter(PermissionsResultListenter permissionsResultListenter){
        mPermissionsResultListenter = permissionsResultListenter;
    }

    /**
     * 权限结果相关接口
     * @description: Created by Boqin on 2017/4/1 17:15
     */
    public interface PermissionsResultListenter{
        /** 
         * 允许权限的回调
         * @param permission
         */
        void onGranted(String permission);
        /**
         * 解决权限的回调
         * @param permission
         */
        void onDenied(String permission);
    }

    private void initDeniedPermissionString(Activity activity, @NonNull String permission) {

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
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                mGrantedList.add(permission);
            }
            mDeniedList.add(permission);
        }
    }

    private void showRationaleDialog(String message) {
        new AlertDialog.Builder(PermissionFragment.this.getActivity())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
//                        request.proceed();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
//                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

}
