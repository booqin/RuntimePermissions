package com.boqin.permissionapi.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限相关的Fragment，有对应的生命周期，同时有获取权限请求结果的回调方法
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionFragment extends Fragment {

    private List<String> mGrantedList = new ArrayList<>();
    private List<String> mDeniedList = new ArrayList<>();
    private List<String> mPermissions = new ArrayList<>();
    /**
     * 是否需要回调标志位
     */
    private Map<String, Boolean> mIsCallBackFlagMap = new HashMap<>();

    /** 权限请求结果回调接口 */
    private PermissionsResultListener mPermissionsResultListener;

    private boolean isNeedRequestPermissions = false;

    @Override
    public void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNeedRequestPermissions) {
            String[] strings = new String[mPermissions.size()];
            mPermissions.toArray(strings);
            doRequestPermissions(strings);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions() {
        isNeedRequestPermissions = false;
        String[] strings = new String[mDeniedList.size()];
        mDeniedList.toArray(strings);
        if (strings.length==0) {
            //没有未允许的情况下
            for (String s : mGrantedList) {
                if(mPermissionsResultListener !=null){
                    mPermissionsResultListener.onGranted(s);
                }
            }
        }else {
            requestPermissions(strings, 0);
        }
    }



    public void doRequestPermissions(@NonNull String[] permissions) {
        mGrantedList.clear();
        mDeniedList.clear();
        mPermissions.clear();
        for (String permission : permissions) {
            initPermissionState(getActivity(), permission);
        }
        requestPermissions();
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
                    mDeniedList.remove(permissions[i]);
                    mGrantedList.add(permissions[i]);
                    break;
                case PackageManager.PERMISSION_DENIED:
                    if(mPermissionsResultListener !=null){
                        mPermissionsResultListener.onDenied(permissions[i]);
                    }
                    break;
                default:
                    break;
            }
        }
        for (String s : mGrantedList) {
            if(mPermissionsResultListener !=null){
                mPermissionsResultListener.onGranted(s);
            }
        }
        //存在未被授权的权限，弹窗提示
        if(mDeniedList.size()!=0){
            showRationaleDialog(mDeniedList.toString());
        }
    }

    /**
     * 设置对应接口
     * @param permissionsResultListener
     */
    public void setPermissionsResultListenter(PermissionsResultListener permissionsResultListener){
        mPermissionsResultListener = permissionsResultListener;
    }

    /**
     * 权限结果相关接口
     * @description: Created by Boqin on 2017/4/1 17:15
     */
    public interface PermissionsResultListener {
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

    private void initPermissionState(Activity activity, @NonNull String permission) {
        mPermissions.add(permission);
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            mDeniedList.add(permission);
        }else {
            mGrantedList.add(permission);
        }
    }

    private void showRationaleDialog(String message) {
        new AlertDialog.Builder(PermissionFragment.this.getActivity())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        Intent localIntent = new Intent();
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", PermissionFragment.this.getActivity().getPackageName(), null));
                        PermissionFragment.this.getActivity().startActivity(localIntent);
                        isNeedRequestPermissions = true;
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        PermissionFragment.this.getActivity().finish();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

}
