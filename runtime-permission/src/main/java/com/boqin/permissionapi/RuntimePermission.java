package com.boqin.permissionapi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import com.boqin.permissionapi.fragment.PermissionFragment;
import com.boqin.runtimepermissions.AnnotationConstant;

import java.lang.reflect.InvocationTargetException;

/**
 * 工具类，封装processor生成代码的访问方法
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */

public class RuntimePermission {

    /**
     * fragment的标签值
     */
    private static final String TAG = "PERMISSION_TAG";


    /**
     * 请求权限操作
     *
     * @param activity                  宿主Activity
     * @param strings                   权限组
     * @param permissionsResultListener 回调接口
     */
    public static void tryPermission(Activity activity, String[] strings, PermissionFragment.PermissionsResultListener permissionsResultListener) {

        initPermissionFragment(activity, strings, permissionsResultListener);
    }

    /**
     * 请求权限操作
     *
     * @param activity 宿主Activity
     */
    public static void tryPermissionByAnnotation(final Activity activity) {
        PermissionFragment.PermissionsResultListener permissionsResultListener = new PermissionFragment.PermissionsResultListener() {
            @Override
            public void onGranted(String permission) {
                doPermissionFeed(activity, permission);
            }

            @Override
            public void onDenied(String permission) {

            }
        };
        String[] strings = getPermissionStringFromAnno(activity);
        if (strings == null) {
            return;
        }
        tryPermission(activity, strings, permissionsResultListener);
    }

    /**
     * 初始化Fragment
     *
     * @param permissionsResultListener 回调接口
     */
    private static void initPermissionFragment(Activity activity, String[] permissions, PermissionFragment.PermissionsResultListener permissionsResultListener) {

        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PermissionFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }

        if (fragment instanceof PermissionFragment) {
            ((PermissionFragment) fragment).setPermissionsResultListenter(permissionsResultListener);
            ((PermissionFragment) fragment).doRequestPermissions(
                    permissions);
        }
    }

    /**
     * 从注解中获取权限名称
     */
    private static String[] getPermissionStringFromAnno(Activity activity) {
        Class cl = getClass(activity);
        String result[] = null;
        try {
            if (cl != null) {
                result = (String[]) cl.getMethod(AnnotationConstant.METHOD_PERMISSION).invoke(cl.newInstance());
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 权限反馈
     */
    private static void doPermissionFeed(Activity activity, String permission) {
        Class cl = getClass(activity);

        try {
            if (cl != null) {
                cl.getMethod(AnnotationConstant.METHOD_PERMISSION_GRANTED, activity.getClass(), String.class)
                        .invoke(cl.newInstance(), activity, permission);
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类
     */
    private static Class getClass(Activity activity) {
        try {
            String className = activity.getClass().getName() + AnnotationConstant.CLASS_SUFFIX;
            return Class.forName(className);

        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
