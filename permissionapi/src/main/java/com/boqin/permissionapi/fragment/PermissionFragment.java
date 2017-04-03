package com.boqin.permissionapi.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 权限相关的Fragment，有对应的生命周期，同时有获取权限请求结果的回调方法
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionFragment extends Fragment {

    /** 权限请求结果回调接口 */
    private PermissionsResultListenter mPermissionsResultListenter;

    @Override
    public void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, 0);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,
            @NonNull
                    String[] permissions,
            @NonNull
                    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        for (int grantResult : grantResults) {
//            switch (grantResult){
//                case PackageManager.PERMISSION_GRANTED:
//                    if(mPermissionsResultListenter!=null){
//                        mPermissionsResultListenter.onGranted();
//                    }
//                    break;
//                case PackageManager.PERMISSION_DENIED:
//                    if(mPermissionsResultListenter!=null){
//                        mPermissionsResultListenter.onDenied();
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
        for (int i = 0; i < grantResults.length; i++) {
            switch (grantResults[i]){
                case PackageManager.PERMISSION_GRANTED:
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

}
