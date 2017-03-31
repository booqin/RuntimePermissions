package com.boqin.permissionapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.boqin.runtimepermissions.BQConstant;

import android.app.Activity;

/**
 * 工具类，封装processor生成代码的访问方法
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionApiUtil {

    public static void doPermission(Activity activity){
        findClass(activity);
    }

    private static void findClass(Activity activity) {
        try {
            String className = activity.getClass().getName()+ BQConstant.CLASS_SUFFIX;
            Class<?> generatedClass = Class.forName(className);
            generatedClass.getConstructors()[0].newInstance(activity);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
