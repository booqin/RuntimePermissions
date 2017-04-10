package com.boqin.permissionapi.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.os.Build;

/**
 * 权限组映射表
 * Created by Boqin on 2017/4/10.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionMapUtil {
    private static final String CALENDAR = "日历";
    private static final String CAMERA = "摄像头";
    private static final String CONTACTS = "联系人";
    private static final String LOCATION = "地址";
    private static final String MICROPHONE = "麦克风";
    private static final String PHONE = "电话";
    private static final String SENSORS = "传感器";
    private static final String SMS = "短信";
    private static final String STORAGE = "存储";

    private static Map<String, String> permissionMap = new HashMap();
    static {
        permissionMap.put(Manifest.permission.READ_CALENDAR, CALENDAR);
        permissionMap.put(Manifest.permission.WRITE_CALENDAR, CALENDAR);
        permissionMap.put(Manifest.permission.CAMERA, CAMERA);
        permissionMap.put(Manifest.permission.READ_CONTACTS, CONTACTS);
        permissionMap.put(Manifest.permission.WRITE_CONTACTS, CONTACTS);
        permissionMap.put(Manifest.permission.GET_ACCOUNTS, CONTACTS);
        permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        permissionMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION);
        permissionMap.put(Manifest.permission.RECORD_AUDIO, MICROPHONE);
        permissionMap.put(Manifest.permission.READ_PHONE_STATE, PHONE);
        permissionMap.put(Manifest.permission.CALL_PHONE, PHONE);

        permissionMap.put(Manifest.permission.ADD_VOICEMAIL, PHONE);
        permissionMap.put(Manifest.permission.USE_SIP, PHONE);
        permissionMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, PHONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionMap.put(Manifest.permission.WRITE_CALL_LOG, PHONE);
            permissionMap.put(Manifest.permission.READ_CALL_LOG, PHONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                permissionMap.put(Manifest.permission.BODY_SENSORS, SENSORS);
            }
            permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE);
        }

        permissionMap.put(Manifest.permission.SEND_SMS, SMS);
        permissionMap.put(Manifest.permission.RECEIVE_SMS, SMS);
        permissionMap.put(Manifest.permission.READ_SMS, SMS);
        permissionMap.put(Manifest.permission.RECEIVE_WAP_PUSH, SMS);
        permissionMap.put(Manifest.permission.RECEIVE_MMS, SMS);
        permissionMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE);
    }

    private static String getPermissionName(String permission){
        return permissionMap.get(permission);
    }

    /**
     * 获取映射拼接后的结果
     * @param permissionList 权限全名列表
     */
    public static String getPermissionNames(List<String> permissionList){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : permissionList) {
            if (PermissionMapUtil.getPermissionName(s)!=null) {
                stringBuilder.append(PermissionMapUtil.getPermissionName(s) +",");
            }
        }
        if (stringBuilder.length()!=0) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }
}
