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
     * 检查权限
     */
    private static void checkPermission(Activity activity, @NonNull String[] permissions) {
//        String permission = getPermissionString(activity);
//        List<String> list = new ArrayList<>();
//        for (String permission : permissions) {
//            String pms = getRequestPermissionStrings(activity, permission);
//            if (pms != null) {
//                list.add(pms);
//            }
//        }
//        String[] strings = new String[list.size()];
//        list.toArray(strings);
//        if (strings.length>0) {
//
//        }
        doRequest(activity, permissions);
    }

    private static String getRequestPermissionStrings(Activity activity, @NonNull String permission) {
        String requestPermmision = null;
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
                requestPermmision = permission;
                // No explanation needed, we can request the permission.
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return requestPermmision;
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
    private static void doPermissionFeed(Activity activity){
        Class cl = getClass(activity);
        try {
            cl.getMethod(BQConstant.METHOD_PERMISSION_GRANTED, activity.getClass()).invoke(cl.newInstance(), activity);
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
