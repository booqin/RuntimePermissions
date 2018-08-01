package com.boqin.permissionapi.interfaces;

import java.util.List;

/**
 * 权限拒绝后的操作回调，以及明确解决后Rationale操作回调
 * Created by BoQin on 2018/8/1.
 * Modified by BoQin
 *
 * @Version
 */
public interface PermissionsDeniedResultListener {

    /**
     * 解决权限的回调
     * @param permission 被拒绝的权限名
     */
    void onDenied(String permission);

    /**
     * 获取说明信息，在权限被拒绝后提示框中使用
     * @param permissions 被拒绝的权限数组
     */
    String getRationaleMessage(List<String> permissions);

}
