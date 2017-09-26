package com.boqin.permissionapi.config;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * 三方Rom支持（小米）
 * Created by Boqin on 2017/9/26.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionOpsUtil {

    /**
     * 权限检测
     * @param context
     * @param permission 权限
     * @return 不存在为授权，true:已授权，false:未授权
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkOpsPermission(Context context, String permission) {
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String opsName = AppOpsManager.permissionToOp(permission);
            if (opsName == null) {
                return true;
            }
            int opsMode = appOpsManager.checkOpNoThrow(opsName, android.os.Process.myUid(), context.getPackageName());
            return opsMode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            return true;
        }
    }

}
