package com.boqin.runtimepermissions.codegen.bean;

import com.squareup.javapoet.TypeName;

/**
 * TODO
 * Created by Boqin on 2017/4/5.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionsBean {

    private String mPermissionName;

    private String mMethodName;

    private String mSimpleName;

    private TypeName mTypeName;

    public String getPermissionName() {
        return mPermissionName;
    }

    public void setPermissionName(String permissionName) {
        mPermissionName = permissionName;
    }

    public String getMethodName() {
        return mMethodName;
    }

    public void setMethodName(String methodName) {
        mMethodName = methodName;
    }

    public String getSimpleName() {
        return mSimpleName;
    }

    public void setSimpleName(String simpleName) {
        mSimpleName = simpleName;
    }

    public TypeName getTypeName() {
        return mTypeName;
    }

    public void setTypeName(TypeName typeName) {
        mTypeName = typeName;
    }
}
