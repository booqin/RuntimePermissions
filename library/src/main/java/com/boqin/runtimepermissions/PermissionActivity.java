package com.boqin.runtimepermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * custom annotation
 * Created by Boqin on 2017/3/30.
 * Modified by Boqin
 *
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PermissionActivity {
    String[] value();
}
