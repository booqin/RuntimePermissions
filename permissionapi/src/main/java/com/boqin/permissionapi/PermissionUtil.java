package com.boqin.permissionapi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.boqin.permissionapi.fragment.PermissionFragment;
import com.boqin.runtimepermissions.BQConstant;

import java.lang.reflect.InvocationTargetException;

/**
 * Desctiption: TODO
 * Created by Vito on 2017/4/3.
 * Email:developervito@163.com
 * ModifiedBy: Vito
 * ModifiedTime: 2017/4/3 20:09
 * ModifiedNotes: TODO
 * Version 1.0
 */

public class PermissionUtil {
    /**
     * 请求权限操作
     * @param activity 宿主Activity
     */
    public static void tryPermission(Activity activity, PermissionFragment.PermissionsResultListenter permissionsResultListenter){
        initFragment(activity, permissionsResultListenter);
        String [] strings = getPermissionString(activity);
        checkPermission(activity, strings);
    }

    /**
     * 请求权限操作
     * @param activity 宿主Activity
     */
    public static void tryPermissionByAnnotation(final Activity activity){
//        initFragment(activity, permissionsResultListenter);
//        String [] strings = getPermissionString(activity);
//        checkPermission(activity, strings);
        PermissionFragment.PermissionsResultListenter permissionsResultListenter = new PermissionFragment.PermissionsResultListenter() {
            @Override
            public void onGranted(String permission) {
                doPermissionFeed(activity, permission);
            }

            @Override
            public void onDenied(String permission) {

            }
        };
        initFragment(activity, permissionsResultListenter);
        String[] strings = getPermissionString(activity);
        checkPermission(activity, strings);
    }

    /**
     * 检查权限
     */
    private static void checkPermission(Activity activity, @NonNull String[] permissions) {
        doRequest(activity, permissions);
    }

    private static void doRequest(Activity activity, @NonNull String[] permissions) {
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
        ((PermissionFragment)fragment).requestPermissions(
                permissions);
    }

    /**
     * 初始化Fragment
     * @param activity
     * @param permissionsResultListenter 回调接口
     */
    private static void initFragment(Activity activity, PermissionFragment.PermissionsResultListenter permissionsResultListenter) {

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

        if (fragment instanceof PermissionFragment) {
            ((PermissionFragment) fragment).setPermissionsResultListenter(permissionsResultListenter);
        }
    }

    /**
     * 获取权限名称
     */
    private static String[] getPermissionString(Activity activity){
        Class cl = getClass(activity);
        String result[] = null;
        try {
            result = (String[]) cl.getMethod(BQConstant.METHOD_PERMISSION).invoke(cl.newInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 权限反馈
     */
    private static void doPermissionFeed(Activity activity, String permission){
        Class cl = getClass(activity);
        try {
            cl.getMethod(BQConstant.METHOD_PERMISSION_GRANTED, activity.getClass(), String.class).invoke(cl.newInstance(), activity, permission);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类
     */
    private static Class getClass(Activity activity) {
        try {
            String className = activity.getClass().getName() + BQConstant.CLASS_SUFFIX;
            return Class.forName(className);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
