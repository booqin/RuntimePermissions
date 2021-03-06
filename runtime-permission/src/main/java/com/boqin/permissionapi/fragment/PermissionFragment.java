package com.boqin.permissionapi.fragment;

import static com.boqin.runtimepermissions.AnnotationConstant.ALL_GRANTED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boqin.permissionapi.R;
import com.boqin.permissionapi.config.PermissionMapUtil;
import com.boqin.permissionapi.config.PermissionOpsUtil;
import com.boqin.permissionapi.interfaces.PermissionsDeniedResultListener;

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

/**
 * 权限相关的Fragment，有对应的生命周期，同时有获取权限请求结果的回调方法
 * Created by Boqin on 2017/3/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionFragment extends Fragment {

    private static final String POSITIVE_STRING = "确定";
    private static final String NEGATIVE_EXIT = "退出";
    private static final String NEGATIVE_CANCEL = "取消";
    private Set<String> mGrantedList = new HashSet<>();
    private Set<String> mDeniedList = new HashSet<>();
    private Set<String> mPermissions = new HashSet<>();
    private List<String> mGrantedResults = new ArrayList<>();
    /**
     * 是否需要回调标志位
     */
    private boolean mIsMustGranted;

    /** 权限请求结果回调接口 */
    private PermissionsResultListener mPermissionsResultListener;

    private boolean isNeedRequestPermissions = false;

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,
            @NonNull
                    String[] permissions,
            @NonNull
                    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            switch (grantResults[i]) {
                case PackageManager.PERMISSION_GRANTED:
                    mDeniedList.remove(permissions[i]);
                    mGrantedList.add(permissions[i]);
                    break;
                case PackageManager.PERMISSION_DENIED:
                    if (mPermissionsResultListener != null) {
                        mPermissionsResultListener.onDenied(permissions[i]);
                    }
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < permissions.length; i++) {
            if(PermissionOpsUtil.isDeniedOpsPermission(getContext(), permissions[i])){
                mGrantedList.remove(permissions[i]);
                mDeniedList.add(permissions[i]);
            }
        }

        for (String s : mGrantedList) {
            if (mPermissionsResultListener != null) {
                mPermissionsResultListener.onGranted(s);
            }
        }
        //存在未被授权的权限，弹窗提示
        if (mDeniedList.size() != 0) {
            mGrantedResults.clear();
            mGrantedResults.addAll(mDeniedList);
            if (mPermissionsResultListener!=null) {
                String msg = mPermissionsResultListener.getRationaleMessage(mGrantedResults);
                if (msg!=null&&!msg.isEmpty()) {
                    showRationaleDialog(msg);
                    return;
                }
            }

            String pms = PermissionMapUtil.getPermissionNames(mGrantedResults);

            showRationaleDialog(getContext().getResources().getString(R.string.rationale_msg, pms));
        }else {
            //全部通过
            mPermissionsResultListener.onGranted(ALL_GRANTED);
        }
    }

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
//        if (isNeedRequestPermissions&&mIsMustGranted) {
//            String[] strings = new String[mPermissions.size()];
//            mPermissions.toArray(strings);
//            doRequestPermissions(strings, mIsMustGranted);
//        }
        if (isNeedRequestPermissions) {
            String[] strings = new String[mPermissions.size()];
            mPermissions.toArray(strings);
            doRequestPermissions(strings, mIsMustGranted);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions() {
        isNeedRequestPermissions = false;
        String[] strings = new String[mDeniedList.size()];
        mDeniedList.toArray(strings);
        if (strings.length == 0) {
            //没有未允许的情况下
            for (String s : mGrantedList) {
                if (mPermissionsResultListener != null) {
                    mPermissionsResultListener.onGranted(s);
                }
            }
            mPermissionsResultListener.onGranted(ALL_GRANTED);
        } else {
            requestPermissions(strings, 0);
        }
    }

    /**
     * 请求权限
     * @param permissions 需要的权限
     * @param isMustGranted 是否必须允许
     */
    public void doRequestPermissions(
            @NonNull
                    String[] permissions, boolean isMustGranted) {
        mIsMustGranted = isMustGranted;
        mGrantedList.clear();
        mDeniedList.clear();
        mPermissions.clear();
        for (String permission : permissions) {
            initPermissionState(getActivity(), permission);
        }
        requestPermissions();
    }

    /**
     * 设置对应接口
     */
    public void setPermissionsResultListener(PermissionsResultListener permissionsResultListener) {
        mPermissionsResultListener = permissionsResultListener;
    }

    private void initPermissionState(Activity activity,
            @NonNull
                    String permission) {
        mPermissions.add(permission);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED || PermissionOpsUtil.isDeniedOpsPermission(getContext(), permission)) {

                // Should we show an explanation?
                mDeniedList.add(permission);
            } else {
                mGrantedList.add(permission);
            }
        }
    }

    private void showRationaleDialog(String message) {
        new AlertDialog.Builder(PermissionFragment.this.getActivity())
                .setPositiveButton(POSITIVE_STRING, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            @NonNull
                                    DialogInterface dialog, int which) {
                        Intent localIntent = new Intent();
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", PermissionFragment.this.getActivity().getPackageName(), null));
                        PermissionFragment.this.getActivity().startActivity(localIntent);
                        isNeedRequestPermissions = true;
                    }
                })
                .setNegativeButton(mIsMustGranted ? NEGATIVE_EXIT : NEGATIVE_CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            @NonNull
                                    DialogInterface dialog, int which) {
                        if (mIsMustGranted) {
                            PermissionFragment.this.getActivity().finish();
                        } else {
                            dialog.dismiss();
                        }

                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

    /**
     * 权限结果相关接口
     *
     * @description: Created by Boqin on 2017/4/1 17:15
     */
    public interface PermissionsResultListener extends PermissionsDeniedResultListener{

        /**
         * 允许权限的回调
         */
        void onGranted(String permission);
    }

}
