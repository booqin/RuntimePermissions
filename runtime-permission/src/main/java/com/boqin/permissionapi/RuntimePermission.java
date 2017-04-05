package com.boqin.permissionapi;

import java.lang.reflect.InvocationTargetException;

import com.boqin.permissionapi.fragment.PermissionFragment;
import com.boqin.runtimepermissions.AnnotationConstant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;

/**
 * 工具类，封装processor生成代码的访问方法
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */

public class RuntimePermission {

    /** fragment的标签值 */
    private static final String TAG = "PERMISSION_TAG";

    /**
     * 请求权限操作
     *
     * @param activity 宿主Activity
     * @param permissionsResultListenter 回调接口
     */
    public static void tryPermissionByAnnotation(Activity activity, PermissionFragment.PermissionsResultListenter permissionsResultListenter) {
        initFragment(activity, permissionsResultListenter);
        String[] strings = getPermissionString(activity);
        if (strings == null) {
            return;
        }
        checkPermission(activity, strings);
    }

    /**
     * 请求权限操作
     *
     * @param activity 宿主Activity
     */
    public static void tryPermissionByAnnotation(final Activity activity) {
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
        if (strings == null) {
            return;
        }
        checkPermission(activity, strings);
    }

    /**
     * 检查权限
     */
    private static void checkPermission(Activity activity,
            @NonNull
                    String[] permissions) {
        doRequest(activity, permissions);
    }

    private static void doRequest(Activity activity,
            @NonNull
                    String[] permissions) {
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
        ((PermissionFragment) fragment).requestPermissions(
                permissions);
    }

    /**
     * 初始化Fragment
     *
     * @param permissionsResultListenter 回调接口
     */
    private static void initFragment(Activity activity, PermissionFragment.PermissionsResultListenter permissionsResultListenter) {

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
            ((PermissionFragment) fragment).setPermissionsResultListenter(permissionsResultListenter);
        }
    }

    /**
     * 获取权限名称
     */
    private static String[] getPermissionString(Activity activity) {
        Class cl = getClass(activity);
        String result[] = null;
        try {
            if (cl != null) {
                result = (String[]) cl.getMethod(AnnotationConstant.METHOD_PERMISSION).invoke(cl.newInstance());
            }
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
    private static void doPermissionFeed(Activity activity, String permission) {
        Class cl = getClass(activity);

        try {
            if (cl != null) {
                cl.getMethod(AnnotationConstant.METHOD_PERMISSION_GRANTED, activity.getClass(), String.class)
                        .invoke(cl.newInstance(), activity, permission);
            }
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
            String className = activity.getClass().getName() + AnnotationConstant.CLASS_SUFFIX;
            return Class.forName(className);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
