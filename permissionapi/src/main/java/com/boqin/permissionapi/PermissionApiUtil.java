package com.boqin.permissionapi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.boqin.permissionapi.fragment.PermissionFragment;
import com.boqin.runtimepermissions.BQConstant;

import java.lang.reflect.InvocationTargetException;

/**
 * 工具类，封装processor生成代码的访问方法
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionApiUtil {

    /**
     * 请求权限操作
     * @param activity 宿主Activity
     */
    public static void doPermissionByAnnotation(final Activity activity) {
        initFragment(activity, new PermissionFragment.PermissionsResultListenter() {
            @Override
            public void onGranted(String permission) {
                doPermissionFeed(activity);
            }

            @Override
            public void onDenied(String permission) {

            }
        });
        checkPermission(activity);
    }

    public static void tryPermission(String[] permissions, Activity activity, PermissionFragment.PermissionsResultListenter permissionsResultListenter){

    }

    /**
     * 检查权限
     */
    private static void checkPermission(Activity activity) {
        String permission = getPermissionString(activity);
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
                        new String[] {permission});
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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
    private static String getPermissionString(Activity activity){
        Class cl = getClass(activity);
        String result = "";
        try {
            result = (String) cl.getMethod(BQConstant.METHOD_PERMISSION).invoke(cl.newInstance());
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
